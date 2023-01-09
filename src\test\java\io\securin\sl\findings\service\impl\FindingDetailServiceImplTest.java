package io.securin.sl.findings.service.impl;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.DeprecationHandler;
import org.elasticsearch.common.xcontent.NamedXContentRegistry;
import org.elasticsearch.common.xcontent.XContentParser;
import org.elasticsearch.common.xcontent.json.JsonXContent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.securin.sl.findings.model.FindingDetail;
import io.securin.sl.findings.model.UserDtlsDTO;
import io.securin.sl.findings.service.helper.UserServiceHelper;
import io.securin.sl.findings.utils.ElasticSearchUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class FindingDetailServiceImplTest {
	
	@InjectMocks
	private FindingDetailServiceImpl findingsServiceImpl;
	
	@Spy
	private ObjectMapper findingsObjectMapper;
	
	@Mock
	private UserServiceHelper usrServHlpr;

	@Test
	void testGetAllFindings() throws Exception {
		Mockito.when(usrServHlpr.getUserDtlsDTO( 110L)).thenReturn(getUserDtlsStub());
		SearchResponse mockSearchResp = getSearchRespStub(getFindingsDetailESResponseStub());
		FindingDetailServiceImpl findingsService = Mockito.spy(findingsServiceImpl);
		Mockito.doReturn(mockSearchResp.getHits().getHits()).when(findingsService).getESResponse(Mockito.any(),
				Mockito.any());
		FindingDetail findings = findingsService.getFindingDetails( "7f18a41522705310c2a460d61dbf36196c9a6bfe09db277bcafd447f87c19d64", 110L);
		assertEquals("9.8", findings.getScores().getCvssv3Score().getBaseScore());
	}
	
	@Test
	void testFindingBranchDetails() throws Exception {
		Mockito.when(usrServHlpr.getUserDtlsDTO( 110L)).thenReturn(getUserDtlsStub());
		SearchResponse mockSearchResp = getSearchRespStub(getFindingsBranchDetailESResponseStub());
		FindingDetailServiceImpl findingsService = Mockito.spy(findingsServiceImpl);
		Mockito.doReturn(mockSearchResp.getHits().getHits()).when(findingsService).getESResponse(Mockito.any(),
				Mockito.any());
		FindingDetail findings = findingsService.getFindingDetails( "5493a6d5d41e507b79d0e4d5b730da7b7b1a551691a057c917a528dce0400bf5", 110L);
		assertEquals("sample-branch-2", findings.getOtherDetails().getBranch());
	}
	
	private SearchResponse getSearchRespStub(String findingsSummaryResponse) {
		SearchResponse searchResponse = null;
		try {
			NamedXContentRegistry registry = new NamedXContentRegistry(ElasticSearchUtil.getDefaultNamedXContents());
			XContentParser parser = JsonXContent.jsonXContent.createParser(registry,
					DeprecationHandler.THROW_UNSUPPORTED_OPERATION, findingsSummaryResponse);
			searchResponse = SearchResponse.fromXContent(parser);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return searchResponse;
	}
	
	private UserDtlsDTO getUserDtlsStub() {
		UserDtlsDTO userDtlsDTO = new UserDtlsDTO();
		userDtlsDTO.setClientId(100L);
		userDtlsDTO.setEmailId("dasdas@fdf.com");
		userDtlsDTO.setIndex("sl_index_100");
		userDtlsDTO.setOrgId(110L);
		userDtlsDTO.setUsrId("125ebc7f-3f6a-41c6-b18e-8dcc93324f48");
		return userDtlsDTO;
	}
	
	private String getFindingsDetailESResponseStub() throws IOException {
		Path data = Paths.get("src", "test", "resources", "FindingDetailStub.json");
		return Files.readString(data);
	}
	
	private String getFindingsBranchDetailESResponseStub() throws IOException {
		Path data = Paths.get("src", "test", "resources", "FindingBranchStub.json");
		return Files.readString(data);
	}
}
