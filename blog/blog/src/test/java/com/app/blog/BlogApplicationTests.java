package com.app.blog;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Random;

import com.app.blog.dto.UpdatePostDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.app.blog.dto.LoginDto;
import com.app.blog.dto.PostDTO;


@TestMethodOrder(Alphanumeric.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc()
class BlogApplicationTests {
	@LocalServerPort
	int port;
	@Autowired
	private MockMvc mvc;
	@Autowired
	TestRestTemplate template;
	static String user, pass, jwt, postBody, postTitle;
	static int postCount, postId = -1;

	public String generateString() {
		String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		Random random = new Random();
		candidateChars.charAt(random.nextInt(candidateChars.length()));
		String randStr = "";
		while (randStr.length() < 8)
			randStr += candidateChars.charAt(random.nextInt(candidateChars.length()));
		return randStr;
	}


	@Test
	public void test2_login() {
		try {
			LoginDto obj = new LoginDto();
			obj.setEmail("himalaya.saxena@gmail.com");
			obj.setPassword("123456669");
			ResponseEntity<String> result = template.postForEntity("http://localhost:" + port + "/login", obj, String.class);
			JSONObject json = new JSONObject(result.getBody());
			//assertEquals(json.getString("data"), "Invalid Username or Password");
			//obj.setEmail(user + "@gmail.com");
			obj.setPassword("12345");
			json = new JSONObject(
					template.postForEntity("http://localhost:" + port + "/login", obj, String.class).getBody());
			jwt = json.getString("data");
			assert (!jwt.equals(""));
			HttpHeaders headers = new HttpHeaders();
			headers.set("authorization", "Bearer " + (char) ((int) jwt.charAt(0) + 1) + jwt.substring(1, jwt.length()));
			ResponseEntity<String> res = template.exchange("http://localhost:" + port + "/api/getPostCount",
					HttpMethod.GET, new HttpEntity<String>(headers), String.class);
			headers.set("authorization", "Bearer " + jwt);
			json = new JSONObject(template.exchange("http://localhost:" + port + "/api/getPostCount", HttpMethod.GET,
					new HttpEntity<String>(headers), String.class).getBody());
			postCount = json.getInt("data");
			assert (postCount >= 0);
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	public void test4_postCount() {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("authorization", "Bearer " + jwt);
			//.withBasicAuth("himalaya.saxena@gmail.com", "12345")
			JSONObject json = new JSONObject(template.exchange("http://localhost:" + port + "/api/getPostCount",
					HttpMethod.GET, new HttpEntity<String>(headers), String.class).getBody());
			assertEquals(json.getInt("data"), postCount + 14);
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	public void test5_getPostById() {
		try {
			HttpHeaders headers = new HttpHeaders();
			jwt = "eyJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoiSGltYWxheWEiLCJlbWFpbCI6ImhpbWFsYXlhLnNheGVuYUBnbWFpbC5jb20iLCJ1c2VyX2lkIjoxLCJzdWIiOiJoaW1hbGF5YS5zYXhlbmFAZ21haWwuY29tIiwiaWF0IjoxNjkyODkzNTc0LCJpc1VzZXIiOnRydWV9.81t6Qso7PSfpc59N1FN-LIhHvmtI984OKWDS9uG4Wj8";
			headers.set("authorization", "Bearer " + jwt);
			ResponseEntity<String> response = template.exchange("http://localhost:" + port + "/api/getUserPost?postId=" + 1, HttpMethod.GET,
					new HttpEntity<String>(headers), String.class);
			String result = response.getBody();
			ObjectMapper mapper = new ObjectMapper();
			JSONObject json = (JSONObject) (new JSONObject(result));
			assertNotNull(json.getString("data"));
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	public void test6_getPostByUserId() {
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("authorization", "Bearer " + jwt);
			JSONObject json;
			boolean pass = false;
			for (int i = 1; i < 10; i++) {
				String uri = "http://localhost:" + port + "/api/getPostByUser/?userId=" + i;
				json = new JSONObject(template.exchange(uri,
						HttpMethod.GET, new HttpEntity<String>(headers), String.class).getBody());
				if (!json.get("data").toString().contentEquals("No posts by user Id ")
						&& !json.getJSONArray("data").getJSONObject(0).getString("title").isEmpty()
						&& !json.getJSONArray("data").getJSONObject(0).getString("body").isEmpty()) {
					pass = true;
					break;
				}
			}
			assert (pass);
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	public void test7_updatePost() {
		try {
			UpdatePostDTO post = new UpdatePostDTO();
			post.setPost_id(1);
			post.setTitle(postTitle);
			String newBody = generateString();
			post.setBody(newBody);
			HttpHeaders headers = new HttpHeaders();
			headers.set("authorization", "Bearer " + jwt);
			HttpEntity<UpdatePostDTO> request = new HttpEntity<>(post, headers);
			JSONObject json = new JSONObject(template
					.postForEntity("http://localhost:" + port + "/api/updatePost", request, String.class).getBody());
			assertEquals(json.getString("data"), "Post updated ");

		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

	@Test
	public void test8_delPost() {
		try {
			postId = 1;
			HttpHeaders headers = new HttpHeaders();
			headers.set("authorization", "Bearer " + jwt);
			JSONObject json = new JSONObject(template.exchange("http://localhost:" + port + "/api/deletePost/?postId=" + postId,
					HttpMethod.GET, new HttpEntity<String>(headers), String.class).getBody());
			assertEquals(json.getString("data"), "Post Deleted");
		} catch (Exception e) {
			e.printStackTrace();
			assert (false);
		}
	}

}
