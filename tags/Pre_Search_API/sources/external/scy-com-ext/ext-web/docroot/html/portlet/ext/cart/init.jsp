<%@ include file="/html/portlet/asset_publisher/init.jsp" %>
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
<%@ page import="com.ext.portlet.cart.model.Cart" %>
<%@ page import="com.ext.portlet.cart.model.CartEntry" %>
<%@ page import="com.ext.portlet.cart.service.persistence.CartUtil" %>
<%@ page import="com.ext.portlet.cart.service.persistence.CartEntryUtil" %>
<%@ page import="com.ext.portlet.cart.service.CartLocalServiceUtil" %>
<%@ page import="com.ext.portlet.cart.service.CartEntryLocalServiceUtil" %>
<%@ page import="com.liferay.counter.service.CounterLocalServiceUtil" %>
<%@ page import="com.ext.portlet.freestyler.permission.FreestylerEntryPermission" %>
<%@ page import="com.ext.portlet.freestyler.permission.FreestylerPermission" %>
<%@ page import="com.ext.portlet.freestyler.model.FreestylerEntry" %>
<%@ page import="com.ext.portlet.freestyler.model.FreestylerFolder" %>
<%@ page import="com.ext.portlet.freestyler.model.FreestylerImage" %>
<%@ page import="com.ext.portlet.freestyler.service.FreestylerEntryLocalServiceUtil" %>
<%@ page import="com.ext.portlet.freestyler.service.FreestylerFolderLocalServiceUtil" %>
<%@ page import="com.ext.portlet.freestyler.service.FreestylerImageLocalServiceUtil" %>