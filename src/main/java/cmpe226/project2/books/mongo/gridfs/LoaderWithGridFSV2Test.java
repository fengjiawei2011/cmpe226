package cmpe226.project2.books.mongo.gridfs;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class LoaderWithGridFSV2Test {

	@Test
	public void test() throws IOException {
		LoaderWithGridFSV2.load("/Users/lingzhang/Documents/DataSet/books/");
	}

}
