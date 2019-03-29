package com.yoti.nicola.fabbrini.recruitment.integration;

import com.yoti.nicola.fabbrini.recruitment.domain.Coordinate;
import com.yoti.nicola.fabbrini.recruitment.domain.Room;
import com.yoti.nicola.fabbrini.recruitment.domain.Roomba;
import com.yoti.nicola.fabbrini.recruitment.utils.RoomParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback
@Transactional
public class RoombaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testEmptyBody() throws Exception {
        mockMvc.perform(post("/roomba")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testWrongFormat() throws Exception {
        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content("{\"roomSize\": [4, 5],\"coords\": \"3, 3\",\"instructions\": \"NSWE\",\"patches\": [[0, 1],[1, 1],[2, 1]]}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        Assert.assertEquals("FORMAT_NOT_VALID", jsonResponse.getString("error_codename"));
        Assert.assertEquals("The format of the request is not valid. Please read the documentation for an example.", jsonResponse.getString("error_message"));
    }

    @Test
    public void testInvalidRoomSize() throws Exception {
        final Coordinate startPosition = new Coordinate(2,2);
        final String instructions = "NSWE";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        final Room room = new Room(0, 10, patches, roomba);

        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content(RoomParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        Assert.assertEquals("INVALID_ROOM_DIMENSION", jsonResponse.getString("error_codename"));
        Assert.assertEquals("Both width (X) and height (Y) of the room must be greater or equals to 1", jsonResponse.getString("error_message"));
    }

    @Test
    public void testInvalidStartPosition() throws Exception {
        final Coordinate startPosition = new Coordinate(10,2);
        final String instructions = "NSWE";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        final Room room = new Room(10, 10, patches, roomba);

        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content(RoomParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        Assert.assertEquals("INVALID_START_POSITION", jsonResponse.getString("error_codename"));
        Assert.assertEquals("The initial position of the roomba is outside the room perimeter. 0 <= X < width and 0 <= Y < height.", jsonResponse.getString("error_message"));
    }

    @Test
    public void testInvalidInstruction() throws Exception {
        final Coordinate startPosition = new Coordinate(2,2);
        final String instructions = "NSWEO";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        final Room room = new Room(10, 10, patches, roomba);

        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content(RoomParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        Assert.assertEquals("INVALID_INSTRUCTION", jsonResponse.getString("error_codename"));
        Assert.assertEquals("The concatenated instruction string can only contain the following characters: N,S,W,E.", jsonResponse.getString("error_message"));
    }

    @Test
    public void testPatchPosition() throws Exception {
        final Coordinate startPosition = new Coordinate(2,2);
        final String instructions = "NSWE";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        patches.add(new Coordinate(10, 1, true));
        final Room room = new Room(10, 10, patches, roomba);

        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content(RoomParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        Assert.assertEquals("INVALID_PATCH_POSITION", jsonResponse.getString("error_codename"));
        Assert.assertEquals("The position of one or more patches is outside the room perimeter. 0 <= X < width and 0 <= Y < height.", jsonResponse.getString("error_message"));
    }

    @Test
    public void testWithNoCleanedPatches() throws Exception {
        final Coordinate startPosition = new Coordinate(2,2);
        final String instructions = "NNESE";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        final Room room = new Room(10, 10, patches, roomba);

        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content(RoomParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        final JSONArray endPosition = jsonResponse.getJSONArray("coords");
        Assert.assertEquals(4, endPosition.getInt(0));
        Assert.assertEquals(3, endPosition.getInt(1));
        Assert.assertEquals(0, jsonResponse.getInt("patches"));
    }

    @Test
    public void testHittingNorthWall() throws Exception {
        final Coordinate startPosition = new Coordinate(2,8);
        final String instructions = "NNESE";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        final Room room = new Room(10, 10, patches, roomba);

        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content(RoomParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        final JSONArray endPosition = jsonResponse.getJSONArray("coords");
        Assert.assertEquals(4, endPosition.getInt(0));
        Assert.assertEquals(8, endPosition.getInt(1));
        Assert.assertEquals(0, jsonResponse.getInt("patches"));
    }

    @Test
    public void testHittingSouthWall() throws Exception {
        final Coordinate startPosition = new Coordinate(2,1);
        final String instructions = "SSENE";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        final Room room = new Room(10, 10, patches, roomba);

        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content(RoomParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        final JSONArray endPosition = jsonResponse.getJSONArray("coords");
        Assert.assertEquals(4, endPosition.getInt(0));
        Assert.assertEquals(1, endPosition.getInt(1));
        Assert.assertEquals(0, jsonResponse.getInt("patches"));
    }

    @Test
    public void testHittingWestWall() throws Exception {
        final Coordinate startPosition = new Coordinate(1,1);
        final String instructions = "WWWWNN";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        final Room room = new Room(10, 10, patches, roomba);

        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content(RoomParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        final JSONArray endPosition = jsonResponse.getJSONArray("coords");
        Assert.assertEquals(0, endPosition.getInt(0));
        Assert.assertEquals(3, endPosition.getInt(1));
        Assert.assertEquals(0, jsonResponse.getInt("patches"));
    }

    @Test
    public void testHittingEastWall() throws Exception {
        final Coordinate startPosition = new Coordinate(8,1);
        final String instructions = "EEEEEN";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        final Room room = new Room(10, 10, patches, roomba);

        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content(RoomParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        final JSONArray endPosition = jsonResponse.getJSONArray("coords");
        Assert.assertEquals(9, endPosition.getInt(0));
        Assert.assertEquals(2, endPosition.getInt(1));
        Assert.assertEquals(0, jsonResponse.getInt("patches"));
    }

    @Test
    public void testSuccessScenario() throws Exception {
        final Coordinate startPosition = new Coordinate(0,0);
        final String instructions = "NENNEEEESWNNW";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        patches.add(new Coordinate(1,1, true));
        patches.add(new Coordinate(6,2, true));
        patches.add(new Coordinate(1,2, true));
        patches.add(new Coordinate(1,3, true));
        patches.add(new Coordinate(1,7, true));
        patches.add(new Coordinate(9,9, true));
        patches.add(new Coordinate(6,6, true));
        patches.add(new Coordinate(5,5, true));
        patches.add(new Coordinate(0,9, true));

        final Room room = new Room(10, 10, patches, roomba);

        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content(RoomParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        final JSONArray endPosition = jsonResponse.getJSONArray("coords");
        Assert.assertEquals(3, endPosition.getInt(0));
        Assert.assertEquals(4, endPosition.getInt(1));
        Assert.assertEquals(3, jsonResponse.getInt("patches"));
    }

    @Test
    public void testGoingBackOnACleanedPatch() throws Exception {
        final Coordinate startPosition = new Coordinate(7,4);
        final String instructions = "WWNNSENNS";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        patches.add(new Coordinate(1,1, true));
        patches.add(new Coordinate(6,2, true));
        patches.add(new Coordinate(1,2, true));
        patches.add(new Coordinate(1,3, true));
        patches.add(new Coordinate(1,7, true));
        patches.add(new Coordinate(9,9, true));
        patches.add(new Coordinate(6,6, true));
        patches.add(new Coordinate(5,5, true));
        patches.add(new Coordinate(0,9, true));

        final Room room = new Room(10, 10, patches, roomba);

        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content(RoomParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        final JSONArray endPosition = jsonResponse.getJSONArray("coords");
        Assert.assertEquals(6, endPosition.getInt(0));
        Assert.assertEquals(6, endPosition.getInt(1));
        Assert.assertEquals(2, jsonResponse.getInt("patches"));
    }

    @Test
    public void testStartOnTopPatch() throws Exception {
        final Coordinate startPosition = new Coordinate(6,6);
        final String instructions = "SWW";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        patches.add(new Coordinate(1,1, true));
        patches.add(new Coordinate(6,2, true));
        patches.add(new Coordinate(1,2, true));
        patches.add(new Coordinate(1,3, true));
        patches.add(new Coordinate(1,7, true));
        patches.add(new Coordinate(9,9, true));
        patches.add(new Coordinate(6,6, true));
        patches.add(new Coordinate(5,5, true));
        patches.add(new Coordinate(0,9, true));

        final Room room = new Room(10, 10, patches, roomba);

        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content(RoomParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        final JSONArray endPosition = jsonResponse.getJSONArray("coords");
        Assert.assertEquals(4, endPosition.getInt(0));
        Assert.assertEquals(5, endPosition.getInt(1));
        Assert.assertEquals(2, jsonResponse.getInt("patches"));
    }

    @Test
    public void testTheDumbCornerer() throws Exception {
        final Coordinate startPosition = new Coordinate(7,8);
        final String instructions = "EEEEENNNNNNNNNWWWWWWWWWWWWWWWWWWWWWWWWWWWW";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        patches.add(new Coordinate(1,1, true));
        patches.add(new Coordinate(6,2, true));
        patches.add(new Coordinate(1,2, true));
        patches.add(new Coordinate(1,3, true));
        patches.add(new Coordinate(1,7, true));
        patches.add(new Coordinate(9,9, true));
        patches.add(new Coordinate(6,6, true));
        patches.add(new Coordinate(5,5, true));
        patches.add(new Coordinate(0,9, true));

        final Room room = new Room(10, 10, patches, roomba);

        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content(RoomParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        final JSONArray endPosition = jsonResponse.getJSONArray("coords");
        Assert.assertEquals(0, endPosition.getInt(0));
        Assert.assertEquals(9, endPosition.getInt(1));
        Assert.assertEquals(2, jsonResponse.getInt("patches"));
    }

    @Test
    public void testReadmeExample() throws Exception {
        final Coordinate startPosition = new Coordinate(1,2);
        final String instructions = "NNESEESWNWW";
        final Roomba roomba = new Roomba(startPosition, instructions);

        final List<Coordinate> patches = new ArrayList<>();
        patches.add(new Coordinate(1,0, true));
        patches.add(new Coordinate(2,2, true));
        patches.add(new Coordinate(2,3, true));

        final Room room = new Room(5, 5, patches, roomba);

        final MvcResult mvcResult = mockMvc.perform(post("/roomba")
                .content(RoomParser.getJSONObjectFromModel(room).toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();

        final String responseBody = mvcResult.getResponse().getContentAsString();
        final JSONObject jsonResponse = new JSONObject(responseBody);

        final JSONArray endPosition = jsonResponse.getJSONArray("coords");
        Assert.assertEquals(1, endPosition.getInt(0));
        Assert.assertEquals(3, endPosition.getInt(1));
        Assert.assertEquals(1, jsonResponse.getInt("patches"));
    }
}
