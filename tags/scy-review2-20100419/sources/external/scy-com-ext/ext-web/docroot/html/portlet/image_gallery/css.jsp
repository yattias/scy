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

.image-popup .taglib-icon-list {
	float: right;
}

.image-popup .taglib-icon-list li {
	margin-left: 1em;
	margin-right: 0;
}

.portlet-image-gallery .asset-abstract .asset-content p {
	margin-bottom: 0;
}

.portlet-image-gallery .asset-content .asset-description {
	font-style: italic;
}

.portlet-image-gallery .asset-content img {
	float: left;
	margin: 3px 10px 0 0;
}

.portlet-image-gallery .asset-content {
	margin-bottom: 10px;
	margin-left: 25px;
	margin-right: 10px;
}

.portlet-image-gallery .asset-full-content .asset-content {
	margin-right: 25px;
}

.portlet-image-gallery .asset-full-content.show-asset-title .asset-content {
	margin-right: 10px;
}

.portlet-image-gallery .asset-edit {
	text-align: right;
}

.portlet-image-gallery .asset-metadata span {
	float: left;
}

.portlet-image-gallery .asset-metadata {
	clear: both;
	margin-left: 25px;
}

.portlet-image-gallery .asset-more {
	clear: left;
}

.portlet-image-gallery .asset-tag-label {
	background-color: #EEE;
	clear: both;
	padding: 3px;
}

.portlet-image-gallery .asset-title .asset-actions img {
	margin-left: 5px;
}

.portlet-image-gallery .asset-title .asset-actions {
	float: right;
	font-size: 11px;
	font-weight: normal;
	margin-bottom: 3px;
	margin-top: 0;
}

.portlet-image-gallery .asset-title a {
	text-decoration: none;
}

.portlet-image-gallery .asset-title a:hover {
	text-decoration: underline;
}

.portlet-image-gallery .asset-title {
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

.portlet-image-gallery .blog {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/blogs.png);
}

.portlet-image-gallery .bookmark {
	background-image: url(<%= themeImagesPath %>/ratings/star_hover.png);
}

.portlet-image-gallery .content {
	background-image: url(<%= themeImagesPath %>/common/history.png);
}

.portlet-image-gallery .dl-file-icon {
	margin: 0 5px 0 0;
}

.portlet-image-gallery .document {
	background-image: url(<%= themeImagesPath %>/common/clip.png);
}

.portlet-image-gallery .edit-controls {
	margin-bottom: 20px;
}

.portlet-image-gallery .final-separator {
	border: 0;
	margin-bottom: 30px;
}

.portlet-image-gallery .image {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/image_gallery.png);
}

.portlet-image-gallery .lfr-meta-actions {
	padding-top: 0;
}

.portlet-image-gallery .metadata-author {
	background: url(<%= themeImagesPath %>/portlet/edit_guest.png) no-repeat 0 0;
	float: left;
	font-weight: bold;
	margin-right: 10px;
	padding-left: 25px;
}

.portlet-image-gallery .metadata-entry {
	color: #999;
	padding-bottom: 20px;
}

.portlet-image-gallery .metadata-modified-date, .portlet-image-gallery .metadata-create-date, .portlet-image-gallery .metadata-publish-date, .portlet-image-gallery .metadata-expiration-date {
	background: url(<%= themeImagesPath %>/common/date.png) no-repeat 0 0;
	color: #999;
	margin-bottom: 1em;
	padding-left: 25px;
}

.portlet-image-gallery .metadata-priority {
	background: url(<%= themeImagesPath %>/common/top.png) no-repeat 0 20%;
	margin-right: 10px;
	padding-left: 25px;
}

.portlet-image-gallery .metadata-view-count {
	margin-right: 10px;
}

.portlet-image-gallery .separator {
	border-right: 1px solid #999;
	clear: both;
	margin: 25px 25px;
}

.portlet-image-gallery .taglib-tags-summary {
	float: left;
}

.portlet-image-gallery .thread {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/message_boards.png);
}

.portlet-image-gallery .title-list .asset-actions {
	left: 10px;
	position: relative;
}

.portlet-image-gallery .title-list .asset-metadata {
	padding: 0;
}

.portlet-image-gallery .title-list a {
	float: left;
}

.portlet-image-gallery .vertical-separator {
	border-right: 1px solid #999;
	float: left;
	margin: 0 10px;
	padding: 7px 0;
}

.portlet-image-gallery .wiki {
	background-image: url(<%= themeImagesPath %>/common/pages.png);
}

.portlet-image-gallery li.title-list {
	background-position: 0 0;
	background-repeat: no-repeat;
	clear: both;
	list-style: none;
	margin-bottom: 0.15em;
	margin-right: 8px;
	padding-bottom: 1px;
	padding-left: 25px;
}

.portlet-image-gallery ul.title-list {
	margin-left: 1em;
}