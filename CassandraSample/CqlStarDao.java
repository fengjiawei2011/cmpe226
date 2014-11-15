package com.rideo.app.dao;

import static com.datastax.driver.core.querybuilder.QueryBuilder.bindMarker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rideo.app.model.PageRequest;
import com.rideo.app.model.PagedResult;
import com.rideo.app.model.ScoreAndIdPage;
import com.rideo.app.model.Star;
import com.rideo.app.model.StarView;
import com.rideo.app.model.Tag;

public class CqlStarDao implements StarDao{

	private ObjectMapper mapper;

	private Session session;

	private String keyspace;

	private String starTable;

	private String starRankTable;
	
	private String tagObjectTable;
	
	private final static Logger logger = LoggerFactory.getLogger(CqlStarDao.class);
	
	private PreparedStatement find;

	private PreparedStatement deleteFromStarTable;
	
	private PreparedStatement deleteFromStarRankTable;
	
	private PreparedStatement deleteFromTagObjectTable;

	private PreparedStatement insertIntoStarTable;
	
	private PreparedStatement insertIntoStarRankTable;

	private Statement findAll;

	private PreparedStatement pagedFindAll;
	
	private PreparedStatement findAllFirstPage;
	
	public CqlStarDao(Session session, ObjectMapper mapper) {
		super();
		this.mapper = mapper;
		this.session = session;
	}

	public void createSchema(){
		String createKeyspace = "CREATE KEYSPACE IF NOT EXISTS "+ keyspace+ " WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};";

		String createStarTable = "CREATE TABLE IF NOT EXISTS "+ keyspace+"."+ starTable +" (" +
				"id text PRIMARY KEY," +
				"name text," +
				"image text," +
				"star_in text," +
				"like_count int" +
				");";

		String createStarListTable = "CREATE TABLE IF NOT EXISTS " + keyspace + "." + starRankTable + "(" +
				"type text," +
				"score double," +
				"id text," + 
				"name text," +
				"image text," +
				"like_count int," +
				"PRIMARY KEY(type, score, id)" +
				") WITH CLUSTERING ORDER BY (score DESC, id ASC);";

		session.execute(createKeyspace);
		session.execute(createStarTable);
		session.execute(createStarListTable);
	}
	
	@PostConstruct
	public void init(){
		createSchema();
		
		find = session.prepare(QueryBuilder.select().from(keyspace, starTable).where(QueryBuilder.eq("id", bindMarker())));
		
		deleteFromStarTable = session.prepare(QueryBuilder.delete().from(keyspace, starTable).where(QueryBuilder.eq("id", bindMarker())));
		
		// TODO Use the real score to delete
		deleteFromStarRankTable = session.prepare(QueryBuilder.delete().from(keyspace, starRankTable).where(QueryBuilder.eq("type", "object_count")).and(QueryBuilder.eq("score", 0.0f)).and(QueryBuilder.eq("id", bindMarker())));
		
		deleteFromTagObjectTable = session.prepare(QueryBuilder.delete().from(keyspace, tagObjectTable).where(QueryBuilder.eq("tag_id", bindMarker())));
		
		insertIntoStarTable = session.prepare(QueryBuilder.insertInto(keyspace, starTable).values(
				new String[]{"id","name","image","star_in","like_count"}, 
				new Object[]{bindMarker(),bindMarker(),bindMarker(),bindMarker(),bindMarker()}));

		insertIntoStarRankTable = session.prepare(QueryBuilder.insertInto(keyspace, starRankTable).values(
				new String[]{"type", "score", "id", "name", "image", "like_count"},
				new Object[]{bindMarker(),bindMarker(),bindMarker(),bindMarker(),bindMarker(),bindMarker()}));
		
		findAll = QueryBuilder.select().from(keyspace, starRankTable).where(QueryBuilder.eq("type", "object_count"));
		
		pagedFindAll = session.prepare(String.format("SELECT * FROM %s.%s WHERE type= ? AND (score, id) <= (?,?) LIMIT ?", keyspace, starRankTable));
		findAllFirstPage = session.prepare(String.format("SELECT * FROM %s.%s WHERE type= ? LIMIT ?", keyspace, starRankTable));
	}
	
	@Override
	public Star find(String id) {
		Validate.notEmpty(id);
		
		Row r = session.execute(find.bind(id)).one();

		return toStar(r);
	}

	@Override
	public void delete(String id) {
		Validate.notEmpty(id);
		
		// TODO Also delete objects that contain the tag
		BatchStatement bs = new BatchStatement(BatchStatement.Type.UNLOGGED);
		bs.add(deleteFromStarTable.bind(id));
		bs.add(deleteFromStarRankTable.bind(id));
		bs.add(deleteFromTagObjectTable.bind(id));
		
		session.execute(bs);
	}

	@Override
	public Star create(Star star) {
		Validate.notEmpty(star.getId());
		
		BatchStatement bs = new BatchStatement(BatchStatement.Type.UNLOGGED);
		/* Build insert statement into title table */
		try {
			bs.add(insertIntoStarTable.bind(star.getId(), star.getName(), star.getImage(), mapper.writeValueAsString(star.getStarIn()), star.getLikeCount()));
		} catch (JsonProcessingException e) {
			logger.error("Exception when serializing Star value.");
			throw new IllegalStateException("Exception when serializing Star value.");
		}
		
		/* Build insert statement into titleRank table as object_count type */
		bs.add(insertIntoStarRankTable.bind("object_count", 0.0, star.getId(), star.getName(), star.getImage(), star.getLikeCount()));
		
		session.execute(bs);
		
		return star;
	}

	@Override
	public List<StarView> findAll() {
		ResultSet rs = session.execute(findAll);
		
		List<StarView> stars = new ArrayList<>();
		for(Row r : rs){
			stars.add(toStarView(r));
		}
		
		return stars;
	}

	@Override
	public PagedResult<StarView> findAll(PageRequest pageRequest) {
		// Validate PageRequest
		ScoreAndIdPage page;
		if(pageRequest.getPage() == null || pageRequest.getPage() instanceof ScoreAndIdPage){
			page = (ScoreAndIdPage)pageRequest.getPage();
		}
		else{
			throw new UnsupportedOperationException("Only ScoreAndIdPage is supported.");
		}
		
		// Fetch one more row as index of next page
		ResultSet rs = null;
		if(page == null){
			rs = session.execute(findAllFirstPage.bind("object_count", pageRequest.getPageSize() + 1));
		}
		else{
			rs = session.execute(pagedFindAll.bind("object_count", page.getScore(), page.getId(), pageRequest.getPageSize() + 1));
		}
		List<StarView> stars = new ArrayList<>();
		Row lastRow = null;
		for(Row r : rs){
			stars.add(toStarView(r));
			lastRow = r;
		}
		
		// Temporary solution before switching to cassandra 2.0.6
//		ResultSet rs = null;
//		List<StarView> stars = new ArrayList<>();
//		Row lastRow = null;
//		if(page == null){
//			rs = session.execute(findAllFirstPage.bind("object_count", pageRequest.getPageSize() + 1));
//			for(Row r : rs){
//				stars.add(toStarView(r));
//				lastRow = r;
//			}
//		}
//		else{
//			rs = session.execute(pagedFindAll.bind("object_count", page.getScore(), 100));
//			boolean add = false;
//			for(Row r : rs){
//				if(add == false && (r.getDouble("score") < page.getScore() || (r.getDouble("score") == page.getScore() && r.getString("id").compareTo(page.getId()) >= 0))){
//					add = true;
//				}
//				if(add == true){
//					stars.add(toStarView(r));
//				}
//				if(stars.size() == pageRequest.getPageSize() + 1){
//					lastRow = r;
//					break;
//				}
//			}
//		}		
		
		// Prepare result
		PagedResult<StarView> result = new PagedResult<>();
		result.setCurPage(page);
		result.setResult(stars);
		if(stars.size() == pageRequest.getPageSize() + 1){
			// Use last row as index
			StarView lastObject = stars.get(stars.size()-1);
			result.setNextPage(new ScoreAndIdPage(lastRow.getDouble("score"), lastObject.getId()));
			// Remove the last row as it does not belong to this page
			stars.remove(stars.size()-1);
		}
		
		return result;
	}
	
	private Star toStar(Row r){
		if(r == null){
			return null;
		}
		
		Star star = new Star();
		star.setId(r.getString("id"));
		star.setName(r.getString("name"));
		star.setImage(r.getString("image"));
		star.setLikeCount(r.getInt("like_count"));
		try {
			star.setStarIn((List<Tag>)mapper.readValue(r.getString("star_in"), new TypeReference<List<Tag>>(){}));
		} catch (IOException e) {
			logger.error("Exception when deserializing star_in value.");
		}
		
		return star;
	}
	
	private StarView toStarView(Row r){
		if(r == null){
			return null;
		}
		
		StarView star = new StarView();
		star.setId(r.getString("id"));
		star.setName(r.getString("name"));
		star.setImage(r.getString("image"));
		star.setLikeCount(r.getInt("like_count"));
		
		return star;
	}

	public String getStarTable() {
		return starTable;
	}

	@Required
	public void setStarTable(String starTable) {
		this.starTable = starTable;
	}

	public String getStarRankTable() {
		return starRankTable;
	}

	@Required
	public void setStarRankTable(String starRankTable) {
		this.starRankTable = starRankTable;
	}	
	
	public String getKeyspace() {
		return keyspace;
	}

	@Required
	public void setKeyspace(String keyspace) {
		this.keyspace = keyspace;
	}

	public String getTagObjectTable() {
		return tagObjectTable;
	}

	public void setTagObjectTable(String tagObjectTable) {
		this.tagObjectTable = tagObjectTable;
	}
}
