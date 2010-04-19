<%@ include file="/html/taglib/init.jsp" %>
<%@ page import="com.liferay.portal.kernel.search.Hits" %>
<%@ page import="com.liferay.portal.kernel.xml.Document" %>
<%@ page import="com.liferay.portal.kernel.xml.Element" %>
<%@ page import="com.liferay.portal.kernel.xml.SAXReaderUtil" %>
<%@ page import="com.liferay.portlet.PortalPreferences" %>
<%@ page import="com.liferay.portlet.tags.NoSuchAssetException" %>
<%@ page import="com.liferay.portlet.tags.NoSuchEntryException" %>
<%@ page import="com.liferay.portlet.tags.NoSuchPropertyException" %>
<%@ page import="com.liferay.portlet.tags.model.TagsAsset" %>
<%@ page import="com.liferay.portlet.tags.model.TagsAssetType" %>
<%@ page import="com.liferay.portlet.tags.model.TagsEntry" %>
<%@ page import="com.liferay.portlet.tags.model.TagsEntryConstants" %>
<%@ page import="com.liferay.portlet.tags.model.TagsProperty" %>
<%@ page import="com.liferay.portlet.tags.model.TagsVocabulary" %>
<%@ page import="com.liferay.portlet.tags.service.TagsAssetLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.tags.service.TagsAssetServiceUtil" %>
<%@ page import="com.liferay.portlet.tags.service.TagsEntryLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.tags.service.TagsPropertyLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.tags.service.TagsVocabularyLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.tags.util.TagsUtil" %>
<%@ page import="com.liferay.portlet.assetpublisher.util.AssetPublisherUtil" %>
<%@ page import="com.liferay.portlet.blogs.model.BlogsEntry" %>
<%@ page import="com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.blogs.service.BlogsEntryServiceUtil" %>
<%@ page import="com.liferay.portlet.blogs.service.permission.BlogsEntryPermission" %>
<%@ page import="com.liferay.portlet.blogs.service.permission.BlogsPermission" %>
<%@ page import="com.liferay.portlet.bookmarks.model.BookmarksEntry" %>
<%@ page import="com.liferay.portlet.bookmarks.model.BookmarksFolder" %>
<%@ page import="com.liferay.portlet.bookmarks.model.impl.BookmarksFolderImpl" %>
<%@ page import="com.liferay.portlet.bookmarks.service.BookmarksEntryLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.bookmarks.service.BookmarksFolderLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.bookmarks.service.permission.BookmarksEntryPermission" %>
<%@ page import="com.liferay.portlet.bookmarks.service.permission.BookmarksFolderPermission" %>
<%@ page import="com.liferay.portlet.bookmarks.service.permission.BookmarksPermission" %>
<%@ page import="com.liferay.portlet.bookmarks.util.BookmarksUtil" %>
<%@ page import="com.liferay.portlet.documentlibrary.model.DLFileEntry" %>
<%@ page import="com.liferay.portlet.documentlibrary.model.DLFolder" %>
<%@ page import="com.liferay.portlet.documentlibrary.model.DLFolderConstants" %>
<%@ page import="com.liferay.portlet.documentlibrary.model.impl.DLFileEntryImpl" %>
<%@ page import="com.liferay.portlet.documentlibrary.model.impl.DLFolderImpl" %>
<%@ page import="com.liferay.portlet.documentlibrary.service.DLFileEntryLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.documentlibrary.service.DLFolderLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.documentlibrary.service.permission.DLFileEntryPermission" %>
<%@ page import="com.liferay.portlet.documentlibrary.service.permission.DLPermission" %>
<%@ page import="com.liferay.portlet.documentlibrary.util.DLUtil" %>
<%@ page import="com.liferay.portlet.documentlibrary.util.DocumentConversionUtil" %>
<%@ page import="com.liferay.portlet.imagegallery.model.IGFolder" %>
<%@ page import="com.liferay.portlet.imagegallery.model.IGImage" %>
<%@ page import="com.liferay.portlet.imagegallery.model.impl.IGFolderImpl" %>
<%@ page import="com.liferay.portlet.imagegallery.service.IGFolderLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.imagegallery.service.IGImageLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.imagegallery.service.permission.IGImagePermission" %>
<%@ page import="com.liferay.portlet.imagegallery.service.permission.IGPermission" %>
<%@ page import="com.liferay.portlet.journalcontent.util.JournalContentUtil" %>
<%@ page import="com.liferay.portlet.journal.NoSuchArticleException" %>
<%@ page import="com.liferay.portlet.journal.action.EditArticleAction" %>
<%@ page import="com.liferay.portlet.journal.model.JournalArticle" %>
<%@ page import="com.liferay.portlet.journal.model.JournalArticleDisplay" %>
<%@ page import="com.liferay.portlet.journal.model.JournalArticleResource" %>
<%@ page import="com.liferay.portlet.journal.model.impl.JournalArticleImpl" %>
<%@ page import="com.liferay.portlet.journal.search.ArticleDisplayTerms" %>
<%@ page import="com.liferay.portlet.journal.search.ArticleSearch" %>
<%@ page import="com.liferay.portlet.journal.search.ArticleSearchTerms" %>
<%@ page import="com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.journal.service.JournalArticleServiceUtil" %>
<%@ page import="com.liferay.portlet.journal.service.JournalArticleResourceLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.journal.service.permission.JournalArticlePermission" %>
<%@ page import="com.liferay.portlet.journal.service.permission.JournalPermission" %>
<%@ page import="com.liferay.portlet.messageboards.model.MBMessage" %>
<%@ page import="com.liferay.portlet.messageboards.service.MBMessageLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.messageboards.service.permission.MBMessagePermission" %>
<%@ page import="com.liferay.portlet.wiki.model.WikiNode" %>
<%@ page import="com.liferay.portlet.wiki.model.WikiPage" %>
<%@ page import="com.liferay.portlet.wiki.model.WikiPageDisplay" %>
<%@ page import="com.liferay.portlet.wiki.model.WikiPageResource" %>
<%@ page import="com.liferay.portlet.wiki.model.impl.WikiPageImpl" %>
<%@ page import="com.liferay.portlet.wiki.service.WikiNodeLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.wiki.service.WikiPageLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.wiki.service.WikiPageResourceLocalServiceUtil" %>
<%@ page import="com.liferay.portlet.wiki.service.permission.WikiPagePermission" %>
<%@ page import="com.liferay.portlet.wiki.util.WikiCacheUtil" %>
<%@ page import="com.liferay.portlet.wiki.util.WikiUtil" %>
<%@ page import="com.liferay.util.xml.DocUtil" %>
<%@ page import="com.ext.portlet.linkTool.model.LinkEntry" %>
<%@ page import="com.ext.portlet.linkTool.service.LinkEntryLocalServiceUtil" %>
<%@ page import="com.liferay.util.LinkToolUtil" %>

<%
String entryId = (String)request.getAttribute("liferay-ui-ext:show-link:classPK");
PortletURL portletURL = (PortletURL)request.getAttribute("liferay-ui-ext:show-link:portletURL");
String struts_action = (String)request.getAttribute("liferay-ui-ext:show-link:strutsAction");
boolean windowStateNormal = true;
List<TagsAsset> results2 = new ArrayList<TagsAsset>();
List<LinkEntry> linkList = LinkEntryLocalServiceUtil.getLinkEntriesByResourceId(Long.valueOf(entryId));
List<LinkEntry> checkedList = new ArrayList<LinkEntry>();
for(LinkEntry link: linkList){
		try {				
			TagsAssetLocalServiceUtil.getAsset(ClassNameLocalServiceUtil.getClassName(Long.valueOf(link.getLinkedResourceClassNameId())).getClassName(), Long.valueOf(link.getLinkedResourceId()));
			checkedList.add(link);
		} catch (Exception e) {
			System.out.println("remove linkEntry for entry with id: " +link.getLinkedResourceId());
			LinkToolUtil.deleteEntry(link);
		}
}
%>
<div>
<br/>
Links:
      				<%
      		    	for (LinkEntry linkEntry : checkedList) {
	      				long classId = Long.valueOf(linkEntry.getLinkedResourceClassNameId());
	      				ClassName className = ClassNameLocalServiceUtil.getClassName(classId);
	      				String classNameId= className.getClassName();
	      				TagsAsset asset = TagsAssetLocalServiceUtil.getAsset(classNameId, Long.valueOf(linkEntry.getLinkedResourceId()));
	      				results2.add(asset);
      				}
   					request.setAttribute("view.jsp-results", results2);
   					request.setAttribute("struts_action", struts_action );
   				      			      				
      				if (results2.size() > 0) {
%>     
							<%@ include file="/html/portlet/ext/links/show_links.jspf" %>			
			
<%
		}
%>
</div>