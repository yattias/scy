<%
/**
 * Copyright (c) 2000-2009 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
%>

<%@ include file="/html/portlet/css_init.jsp" %>

.portlet-document-library .asset-abstract .asset-content p {
	margin-bottom: 0;
}

.portlet-document-library .asset-content .asset-description {
	font-style: italic;
}

.portlet-document-library .asset-content img {
	float: left;
	margin: 3px 10px 0 0;
}

.portlet-document-library .asset-content {
	margin-bottom: 10px;
	margin-left: 25px;
	margin-right: 10px;
}

.portlet-document-library .asset-full-content .asset-content {
	margin-right: 25px;
}

.portlet-document-library .asset-full-content.show-asset-title .asset-content {
	margin-right: 10px;
}

.portlet-document-library .asset-edit {
	text-align: right;
}

.portlet-document-library .asset-metadata span {
	float: left;
}

.portlet-document-library .asset-metadata {
	clear: both;
	margin-left: 25px;
}

.portlet-document-library .asset-more {
	clear: left;
}

.portlet-document-library .asset-tag-label {
	background-color: #EEE;
	clear: both;
	padding: 3px;
}

.portlet-document-library .asset-title .asset-actions img {
	margin-left: 5px;
}

.portlet-document-library .asset-title .asset-actions {
	float: right;
	font-size: 11px;
	font-weight: normal;
	margin-bottom: 3px;
	margin-top: 0;
}

.portlet-document-library .asset-title a {
	text-decoration: none;
}

.portlet-document-library .asset-title a:hover {
	text-decoration: underline;
}

.portlet-document-library .asset-title {
	background-position: 0 50%;
	background-repeat: no-repeat;
	border-bottom: 1px solid #DDD;
	clear: both;
	margin-bottom: 0.15em;
	margin-right: 8px;
	margin-top: 2em;
	padding-bottom: 1px;
	padding-left: 25px;
}

.portlet-document-library .blog {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/blogs.png);
}

.portlet-document-library .bookmark {
	background-image: url(<%= themeImagesPath %>/ratings/star_hover.png);
}

.portlet-document-library .content {
	background-image: url(<%= themeImagesPath %>/common/history.png);
}

.portlet-document-library .dl-file-icon {
	margin: 0 5px 0 0;
}

.portlet-document-library .document {
	background-image: url(<%= themeImagesPath %>/common/clip.png);
}

.portlet-document-library .edit-controls {
	margin-bottom: 20px;
}

.portlet-document-library .final-separator {
	border: 0;
	margin-bottom: 30px;
}

.portlet-document-library .image {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/image_gallery.png);
}

.portlet-document-library .lfr-meta-actions {
	padding-top: 0;
}

.portlet-document-library .metadata-author {
	background: url(<%= themeImagesPath %>/portlet/edit_guest.png) no-repeat 0 0;
	float: left;
	font-weight: bold;
	margin-right: 10px;
	padding-left: 25px;
}

.portlet-document-library .metadata-entry {
	color: #999;
	padding-bottom: 20px;
}

.portlet-document-library .metadata-modified-date, .portlet-document-library .metadata-create-date, .portlet-document-library .metadata-publish-date, .portlet-document-library .metadata-expiration-date {
	background: url(<%= themeImagesPath %>/common/date.png) no-repeat 0 0;
	color: #999;
	margin-bottom: 1em;
	padding-left: 25px;
}

.portlet-document-library .metadata-priority {
	background: url(<%= themeImagesPath %>/common/top.png) no-repeat 0 20%;
	margin-right: 10px;
	padding-left: 25px;
}

.portlet-document-library .metadata-view-count {
	margin-right: 10px;
}

.portlet-document-library .separator {
	border-right: 1px solid #999;
	clear: both;
	margin: 25px 25px;
}

.portlet-document-library .taglib-tags-summary {
	float: left;
}

.portlet-document-library .thread {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/message_boards.png);
}

.portlet-document-library .title-list .asset-actions {
	left: 10px;
	position: relative;
}

.portlet-document-library .title-list .asset-metadata {
	padding: 0;
}

.portlet-document-library .title-list a {
	float: left;
}

.portlet-document-library .vertical-separator {
	border-right: 1px solid #999;
	float: left;
	margin: 0 10px;
	padding: 7px 0;
}

.portlet-document-library .wiki {
	background-image: url(<%= themeImagesPath %>/common/pages.png);
}

.portlet-document-library li.title-list {
	background-position: 0 0;
	background-repeat: no-repeat;
	clear: both;
	list-style: none;
	margin-bottom: 0.15em;
	margin-right: 8px;
	padding-bottom: 1px;
	padding-left: 25px;
}

.portlet-document-library ul.title-list {
	margin-left: 1em;
}