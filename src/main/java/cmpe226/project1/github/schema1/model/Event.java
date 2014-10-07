package cmpe226.project1.github.schema1.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

	@Entity
	@Table(name="event")
	public class Event {
		
		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		@Column(name = "event_id", updatable = false, nullable = false)
		private Long id;
		
		private String url;
		private String type;
		private Date created_at;

				
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
		public Date getCreatedAt() {
			return created_at;
		}
		
		public void setCreatedAt(Date created_at) {
			this.created_at = created_at;
		}
	}


