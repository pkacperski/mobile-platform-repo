// TODO - write tests for real Dtos

//package com.mobileplatform.frontend;
//
//import com.mashape.unirest.http.exceptions.UnirestException;
//import com.mobileplatform.frontend.controller.api.RestHandler;
//import com.mobileplatform.frontend.dto.TestDto;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class RestHandlerTest {
//    private static RestHandler<TestDto> restObjectHandler;
//    private static RestHandler<TestDto[]> restArrayHandler;
//    private static RestHandler<TestDto> restObjectHandlerMock;
//    private static RestHandler<TestDto[]> restArrayHandlerMock;
//    private final String TEST_PATH = "/test";
//    private final String TEST_QUERY_PATH = "/test/query";
//    private final String TEST_ID_PATH_PARAM = "1";
//    private final String TEST_OBJECT_BODY = "{\"name\":\"string\"}";
//    private final String APPLICATION_JSON_CONTENT_TYPE = "application/json";
//    private final Map<String, String> TEST_QUERY_PARAM_MAP = Map.of("id", "3");
//
//    @BeforeAll
//    static void setup() {
//        restObjectHandler = new RestHandler<>(TestDto.class);
//        restArrayHandler = new RestHandler<>(TestDto[].class);
//        restObjectHandlerMock = mock(RestHandler.class);
//        restArrayHandlerMock = mock(RestHandler.class);
//    }
//
//    /**
//     * Following are unit tests - should run fine regardless if Maven build profile is "test" or "prod"
//     * TODO: investigate into mocking Unirest 'get' request to obtain correct response for a correct request
//     */
//
//    @Test
//    void givenSimpleRestGetMethod_whenRequestIsExecuted_thenResponseIsOk_UnitTest() throws UnirestException {
//        TestDto[] expectedResponse = new TestDto[] {
//                TestDto.builder().id(1).name("name1").build(),
//                TestDto.builder().id(2).name("name2").build()
//        };
//        when(restArrayHandlerMock.performGet(TEST_PATH)).thenReturn(expectedResponse);
//        // TODO (rest of unit tests as well) - investigate into mocking Unirest 'get' method:
//        // GetRequest getRequest = Unirest.get(TEST_PATH); // mock? -> NIE: raczej zamochowac odpowiedz (HttpResponse) i z niej wyciagnac!
//        // when(getRequestMock.asJson()).thenReturn(new HttpResponse<>(... tutaj to co w HttpClientHelperze, JsonNode.class));
//        // HttpResponse httpResponseMock = mock(HttpResponse.class); // tez nie
//
//        TestDto[] response = restArrayHandlerMock.performGet(TEST_PATH);
//        assertNotNull(response);
//        assertEquals(2, response.length);
//        assertEquals(1, response[0].getId());
//        assertEquals(2, response[1].getId());
//        assertEquals("name1", response[0].getName());
//        assertEquals("name2", response[1].getName());
//    }
//
//    @Test
//    void givenRestGetMethodWithPathParam_whenRequestIsExecuted_thenResponseIsOk_UnitTest() throws UnirestException {
//        TestDto expectedResponse = TestDto.builder().id(1).name("name1").build();
//        when(restObjectHandlerMock.performGet(TEST_PATH, TEST_ID_PATH_PARAM)).thenReturn(expectedResponse);
//
//        TestDto response = restObjectHandlerMock.performGet(TEST_PATH, TEST_ID_PATH_PARAM);
//        assertNotNull(response);
//        assertEquals(1, response.getId());
//        assertEquals("name1", response.getName());
//    }
//
//    @Test
//    void givenRestGetMethodWithRequestParam_whenRequestIsExecuted_thenResponseIsOk_UnitTest() throws UnirestException {
//        TestDto expectedResponse = TestDto.builder().id(3).name("name3").build();
//        when(restObjectHandlerMock.performGet(TEST_QUERY_PATH, TEST_QUERY_PARAM_MAP)).thenReturn(expectedResponse);
//
//        TestDto response = restObjectHandlerMock.performGet(TEST_QUERY_PATH, TEST_QUERY_PARAM_MAP);
//        assertNotNull(response);
//        assertEquals(3, response.getId());
//        assertEquals("name3", response.getName());
//    }
//
//    @Test
//    void givenRestPostMethodWithBody_whenRequestIsExecuted_thenResponseIsOk_UnitTest() throws UnirestException {
//        TestDto expectedResponse = TestDto.builder().id(1).name("name").build();
//        when(restObjectHandlerMock.performPost(TEST_PATH, TEST_OBJECT_BODY, APPLICATION_JSON_CONTENT_TYPE)).thenReturn(expectedResponse);
//
//        TestDto response = restObjectHandlerMock.performPost(TEST_PATH, TEST_OBJECT_BODY, APPLICATION_JSON_CONTENT_TYPE);
//        assertNotNull(response);
//        assertEquals(1, response.getId());
//        assertEquals("name", response.getName());
//    }
//
//    /**
//     * Following are integration tests:
//     * - when on "test" Maven build profile - to be run with switched on SoapUI Mock on port 9090 (Frontend REST mock)
//     * - when on "prod" Maven build profile - to be run with switched on backend application on port 8080, watch out for database
//     */
//
//    @Test
//    void givenSimpleRestGetMethod_whenRequestIsExecuted_thenResponseIsOk_IntegrationTest() throws UnirestException {
//        TestDto[] response = restArrayHandler.performGet(TEST_PATH);
//        assertNotNull(response);
//        assertNotEquals(0, response.length);
//    }
//
//    @Test
//    void givenRestGetMethodWithPathParam_whenRequestIsExecuted_thenResponseIsOk_IntegrationTest() throws UnirestException {
//        TestDto response = restObjectHandler.performGet(TEST_PATH, TEST_ID_PATH_PARAM);
//        assertNotNull(response);
//        assertEquals(1, response.getId());
//        assertEquals("test", response.getName());
//    }
//
//    /*
//    * Test below (GET method with request (query) param) works only on 'prod' profile - does not work on 'test' profile because of a known issue with SoapUI.
//    * Link to the issue and its solution: https://community.smartbear.com/t5/ReadyAPI-Questions/REST-Mocking-with-Query-Parametr/td-p/97939
//    private final String TEST_QUERY_PATH = "/test/query";
//    private final Map<String, String> TEST_QUERY_PARAM_MAP = Map.of("id", "3");
//    @Test
//    void givenRestGetMethodWithRequestParam_whenRequestIsExecuted_thenResponseIsOk_IntegrationTest() throws UnirestException {
//        TestDto response = restObjectHandler.performGet(TEST_QUERY_PATH, TEST_QUERY_PARAM_MAP);
//        assertNotNull(response);
//        assertEquals(3, response.getId());
//        assertEquals("string", response.getName());
//    }
//    * */
//
//    @Test
//    void givenRestPostMethodWithBody_whenRequestIsExecuted_thenResponseIsOk_IntegrationTest() throws UnirestException {
//        TestDto response = restObjectHandler.performPost(TEST_PATH, TEST_OBJECT_BODY, APPLICATION_JSON_CONTENT_TYPE);
//        assertNotNull(response);
//        assertNotNull(response.getId());
//        assertEquals("string", response.getName());
//    }
//}
