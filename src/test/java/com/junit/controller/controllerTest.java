package com.junit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.junit.model.Books;
import com.junit.services.BooksService;

public class controllerTest {

	@Mock
	BooksService booksService;

	@InjectMocks
	BooksController booksController;
	
	private MockMvc mockMvc;

	ObjectMapper objectMapper = new ObjectMapper();
	
	
	Books book;
	List<Books> list;
	
	@BeforeEach
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(booksController).build();
		book = getBook();
		list=getBookList();
	
	}
	
	
	
	@Test
	public void saveBookTest() throws Exception {
		String jsonRequest = objectMapper.writeValueAsString(book);
		when(booksService.saveOrUpdate(Mockito.any(Books.class))).thenReturn(book);
		MvcResult mvcResult = mockMvc
				.perform(post("/saveBook").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String expectedOutput = mvcResult.getResponse().getContentAsString();
		Books expectedOutputUser = objectMapper.readValue(expectedOutput, Books.class);
		assertEquals(expectedOutputUser.getPrice(), book.getPrice());
	}
	
	
	@Test
	public void updateBookTest() throws Exception {
		String jsonRequest = objectMapper.writeValueAsString(book);
		when(booksService.update(Mockito.any(Books.class),Mockito.anyInt())).thenReturn(book);
		MvcResult mvcResult = mockMvc
				.perform(put("/updateBook").content(jsonRequest).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk()).andReturn();
		String expectedOutput = mvcResult.getResponse().getContentAsString();
		Books expectedOutputUser = objectMapper.readValue(expectedOutput, Books.class);
		assertEquals(expectedOutputUser.getPrice(), book.getPrice());
	}
	
	
	@Test
	public void deleteBookTest() throws Exception {
		String jsonRequest = String.valueOf(book.getBookid());
		when(booksService.getBooksById(Mockito.anyInt())).thenReturn(book);
		MvcResult mvcResult = mockMvc
			.perform(delete("/deleteBook/{bookid}",jsonRequest))
				.andExpect(status().isOk()).andReturn();
		String expectedOutput = mvcResult.getResponse().getContentAsString();
		int id=Integer.parseInt(expectedOutput);
		assertEquals(id, book.getBookid());
	}
	
	
	@Test
	public void getBookByIdTest() throws Exception {
		String jsonRequest = String.valueOf(book.getBookid());
		when(booksService.getBooksById(Mockito.anyInt())).thenReturn(book);
		MvcResult mvcResult = mockMvc
			.perform(get("/getBookById/{bookid}",jsonRequest))
				.andExpect(status().isOk()).andReturn();
		String expectedOutput = mvcResult.getResponse().getContentAsString();
		Books expectedOutputUser = objectMapper.readValue(expectedOutput, Books.class);
		assertEquals(expectedOutputUser.getPrice(), book.getPrice());
	}
	
	
	@Test
	public void getAllBooksTest() throws Exception {
//		when(booksService.getAllBooks().thenReturn(list);
		MvcResult mvcResult = mockMvc
				.perform(get("/getAllBooks"))
				.andExpect(status().isOk()).andReturn();
		int status=mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	
	public Books getBook()
	{
		Books book=new Books();
		book.setBookid(55);
		book.setBookname("hhh");
		book.setAuthor("jjj");
		book.setPrice(90);
		return book;
	}
	public List<Books> getBookList()
	{
		List<Books> bookList = new ArrayList<>();
		
		Books book1=new Books();
		book1.setBookid(11);
		book1.setBookname("cccc");
		book1.setAuthor("ddd");
		book1.setPrice(100);
		
		Books book2=new Books();
		book2.setBookid(12);
		book2.setBookname("fff");
		book2.setAuthor("eeee");
		book2.setPrice(100);
		
		bookList.add(book1);
		bookList.add(book2);
		return bookList;
		
	}
}
