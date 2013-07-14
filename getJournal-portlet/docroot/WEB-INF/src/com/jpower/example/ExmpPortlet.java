package com.jpower.example;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil;
import com.liferay.portlet.dynamicdatalists.model.DDLRecord;
import com.liferay.portlet.dynamicdatalists.service.DDLRecordLocalServiceUtil;
import com.liferay.portlet.dynamicdatamapping.storage.Fields;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

public class ExmpPortlet extends MVCPortlet {

	public void init() throws PortletException {

		// Edit Mode Pages
//		editJSP = getInitParameter("edit-jsp");

		// Help Mode Pages
//		helpJSP = getInitParameter("help-jsp");

		// View Mode Pages
		viewJSP = getInitParameter("view-template");

		// View Mode Edit UserTypes
//		edituserTypesJSP = getInitParameter("edit-userTypes-jsp");

		// View Mode Entry UserTypes
//		viewuserTypesJSP = getInitParameter("view-userTypes-jsp");
	}
	
	public void doView(RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {

		renderRequest.setAttribute("greeting", "Hello world");
		System.out.println("viewJSP : " + viewJSP);

		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest
				.getAttribute(WebKeys.THEME_DISPLAY);

		long groupId = themeDisplay.getScopeGroupId();
		long userId = themeDisplay.getUserId();
		long companyGroupId = themeDisplay.getCompanyGroupId();
		long companyId = themeDisplay.getCompanyId();
		String languageId = themeDisplay.getLanguageId();

		System.out
				.println("ThemeDisplay.getCompanyGroupId : " + companyGroupId);
		System.out.println("ThemeDisplay.getScopeGroupID : " + groupId);
		System.out.println("ThemeDisplay.getUserID : " + userId);
		System.out.println("ThemeDisplay.companyId : " + companyId);
		System.out.println("ThemeDisplay.languageId : " + languageId);

		try {
			List<JournalArticle> allArticles = JournalArticleLocalServiceUtil
					.getArticles();
			for (JournalArticle j : allArticles) {
				System.out.println("ArticleId : " + j.getArticleId());
				System.out.println("Description : " + j.getDescription());
				System.out.println("Title : " + j.getTitle());
				System.out.println("Content : " + j.getContent());
			}

			long ddlRecordCount = DDLRecordLocalServiceUtil
					.getDDLRecordsCount();
			System.out.println("getDDLRecordsCount : " + ddlRecordCount);

//			List<DDLRecord> ddlRecords = DDLRecordLocalServiceUtil
//					.getRecords(10802);
			
			List<DDLRecord> ddlRecords = DDLRecordLocalServiceUtil.getDDLRecords(-1, -1);

//			DDLRecord r = (DDLRecord) ddlRecords.get(0);
//			Fields f = r.getFields();
//			Set<String> fieldNames = f.getNames();
//			System.out.println("Field name : " + fieldNames);
			
			long id = 0;
			for (DDLRecord d : ddlRecords) {	
				
				Fields f = d.getFields();
				Set<String> fieldNames = f.getNames();
				System.out.println("Field name : " + fieldNames);
				
				System.out.print(++id + " (" + d.getRecordId() + " - " + d.getStatus() + ") : ");
				Iterator<String> fieldNameIt = fieldNames.iterator();
				while (fieldNameIt.hasNext()) {
					String fieldName = fieldNameIt.next();
					System.out
							.print("[" + d.getFieldValue(fieldName) + "] - (");
					System.out.print(d.getFieldDataType(fieldName) + "), ");
					
					if(d.getFieldDataType(fieldName).toString().equals("file-upload")) {
						System.out.println("This is image upload");
						JSONObject jObj = JSONFactoryUtil.createJSONObject(d.getFieldValue(fieldName).toString());
						
						System.out.println("JSONArray.toString : " + jObj.toString());
						System.out.println("file-upload : image : path : [" + jObj.getString("path") + "]");
											
					}
				}
				System.out.print("\n");
				
				System.out.println("DDLRecord.toString : " + d.toString() + "\n");
				
			}
			
			System.out.println("\n\n ======================= DLFileEntryLocalServiceUtil ===================\n");
			List<DLFileEntry> dlFileList = DLFileEntryLocalServiceUtil.getFileEntries(-1, -1);	
			System.out.println("DLFileEntryLocalServiceUtil : " 
					+ dlFileList.size());
			if (dlFileList.size() > 0) {
				for (DLFileEntry dlFile : dlFileList) {
					System.out.println(dlFile.toString());
				}
			}

//			System.out.println("============= JSONObject ===========");
//			JSONObject jsonObj = JSONFactoryUtil.createJSONObject();
//			jsonObj.put("additionalInfo", "additionalInfo");
//			jsonObj.put("companyId", "companyID");
//			System.out.println(jsonObj.toString());
//			
			

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		include(viewJSP, renderRequest, renderResponse);
	}
	
	protected void include(String path, RenderRequest renderRequest,
			RenderResponse renderResponse) throws IOException, PortletException {

		PortletRequestDispatcher portletRequestDispatcher = getPortletContext()
				.getRequestDispatcher(path);

		if (portletRequestDispatcher == null) {
			// do nothing
			// _log.error(path + " is not a valid include");
		} else {
			portletRequestDispatcher.include(renderRequest, renderResponse);
		}
	}

	private static Log _log = LogFactoryUtil.getLog(ExmpPortlet.class);
	
//	protected String edituserTypesJSP;
//	protected String editJSP;
//	protected String helpJSP;
	protected String viewJSP;
//	protected String viewuserTypesJSP;

}
