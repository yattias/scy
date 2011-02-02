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

.portlet-blogs .comments {
}

.portlet-blogs .edit-actions {
	margin: 1.5em 0;
}

.portlet-blogs .entry-author {
	background: url(<%= themeImagesPath %>/portlet/edit_guest.png) no-repeat 0 50%;
	border-right: 1px solid #999;
	color: #999;
	float: left;
	font-weight: bold;
	margin-right: 10px;
	padding-left: 25px;
	padding-right: 10px;
}

.portlet-blogs .entry-body {
	margin-bottom: 10px;
}

.portlet-blogs .entry-content {
}

.portlet-blogs .entry-date {
	background: url(<%= themeImagesPath %>/common/date.png) no-repeat 0 50%;
	color: #999;
	margin-bottom: 1em;
	padding-left: 25px;
}

.portlet-blogs .entry-tags {
	clear: both;
	margin: 1.5em 0 0 0;
}

.portlet-blogs .entry-title {
	display: block;
	font-size: 1.5em;
	font-weight: bold;
	margin-bottom: 0.5em;
}

.portlet-blogs .entry.draft {
	background: #eee;
	border: 1px solid #ccc;
	color: #555;
	padding: 5px;
}

.portlet-blogs .entry.draft h3 {
	background: url(<%= themeImagesPath %>/common/page.png) no-repeat 0 50%;
	margin-top: 0;
	padding-left: 20px;
}

.portlet-blogs .entry-navigation {
	background: #eee;
	border-top: 1px solid #ccc;
	margin: 15px 0 0;
	overflow: hidden;
	padding: 5px;
}

.portlet-blogs .entry-navigation a, .portlet-blogs .entry-navigation span {
	background: url() no-repeat;
}

.portlet-blogs .entry-navigation .previous {
	background-image: url(<%= themeImagesPath %>/arrows/paging_previous.png);
	float: left;
	padding-left: 15px;
}

.portlet-blogs .entry-navigation span.previous {
	background-position: 0 100%;
}

.portlet-blogs .entry-navigation .next {
	background-image: url(<%= themeImagesPath %>/arrows/paging_next.png);
	background-position: 100% 0;
	float: right;
	padding-right: 15px;
}

.portlet-blogs .entry-navigation span.next {
	background-position: 100% 100%;
}

.portlet-blogs .search-form {
	float: right
}

.portlet-blogs .stats {
	color: #999;
	float: left;
	padding-right: 10px;
}

.portlet-blogs .subscribe {
	margin-bottom: 1.5em;
}

.portlet-blogs .taglib-flags {
	border-left: 1px solid #999;
	color: #999;
	float: left;
	padding-left: 10px;
}

.portlet-blogs .taglib-ratings.stars {
	margin-top: 0.5em;
}

.portlet-blogs .taglib-social-bookmarks {
	margin-top: 1.5em;
}

.portlet-blogs .taglib-social-bookmarks ul {
	padding: 1em;
}

.portlet-blogs .view-count {
}

.portlet-blogs .asset-abstract .asset-content p {
	margin-bottom: 0;
}

.portlet-blogs .asset-content .asset-description {
	font-style: italic;
}

.portlet-blogs .asset-content img {
	float: left;
	margin: 3px 10px 0 0;
}

.portlet-blogs .asset-content {
	margin-bottom: 10px;
	margin-left: 25px;
	margin-right: 10px;
}

.portlet-blogs .asset-full-content .asset-content {
	margin-right: 25px;
}

.portlet-blogs .asset-full-content.show-asset-title .asset-content {
	margin-right: 10px;
}

.portlet-blogs .asset-edit {
	text-align: right;
}

.portlet-blogs .asset-metadata span {
	float: left;
}

.portlet-blogs .asset-metadata {
	clear: both;
	margin-left: 25px;
}

.portlet-blogs .asset-more {
	clear: left;
}

.portlet-blogs .asset-tag-label {
	background-color: #EEE;
	clear: both;
	padding: 3px;
}

.portlet-blogs .asset-title .asset-actions img {
	margin-left: 5px;
}

.portlet-blogs .asset-title .asset-actions {
	float: right;
	font-size: 11px;
	font-weight: normal;
	margin-bottom: 3px;
	margin-top: 0;
}

.portlet-blogs .asset-title a {
	text-decoration: none;
}

.portlet-blogs .asset-title a:hover {
	text-decoration: underline;
}

.portlet-blogs .asset-title {
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

.portlet-blogs .blog {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/blogs.png);
}

.portlet-blogs .bookmark {
	background-image: url(<%= themeImagesPath %>/ratings/star_hover.png);
}

.portlet-blogs .content {
	background-image: url(<%= themeImagesPath %>/common/history.png);
}

.portlet-blogs .dl-file-icon {
	margin: 0 5px 0 0;
}

.portlet-blogs .document {
	background-image: url(<%= themeImagesPath %>/common/clip.png);
}

.portlet-blogs .edit-controls {
	margin-bottom: 20px;
}

.portlet-blogs .final-separator {
	border: 0;
	margin-bottom: 30px;
}

.portlet-blogs .image {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/image_gallery.png);
}

.portlet-blogs .lfr-meta-actions {
	padding-top: 0;
}

.portlet-blogs .metadata-author {
	background: url(<%= themeImagesPath %>/portlet/edit_guest.png) no-repeat 0 0;
	float: left;
	font-weight: bold;
	margin-right: 10px;
	padding-left: 25px;
}

.portlet-blogs .metadata-entry {
	color: #999;
	padding-bottom: 20px;
}

.portlet-blogs .metadata-modified-date, .portlet-blogs .metadata-create-date, .portlet-blogs .metadata-publish-date, .portlet-blogs .metadata-expiration-date {
	background: url(<%= themeImagesPath %>/common/date.png) no-repeat 0 0;
	color: #999;
	margin-bottom: 1em;
	padding-left: 25px;
}

.portlet-blogs .metadata-priority {
	background: url(<%= themeImagesPath %>/common/top.png) no-repeat 0 20%;
	margin-right: 10px;
	padding-left: 25px;
}

.portlet-blogs .metadata-view-count {
	margin-right: 10px;
}

.portlet-blogs .separator {
	border-right: 1px solid #999;
	clear: both;
	margin: 25px 25px;
}

.portlet-blogs .taglib-tags-summary {
	float: left;
}

.portlet-blogs .thread {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/message_boards.png);
}

.portlet-blogs .title-list .asset-actions {
	left: 10px;
	position: relative;
}

.portlet-blogs .title-list .asset-metadata {
	padding: 0;
}

.portlet-blogs .title-list a {
	float: left;
}

.portlet-blogs .vertical-separator {
	border-right: 1px solid #999;
	float: left;
	margin: 0 10px;
	padding: 7px 0;
}

.portlet-blogs .wiki {
	background-image: url(<%= themeImagesPath %>/common/pages.png);
}

.portlet-blogs li.title-list {
	background-position: 0 0;
	background-repeat: no-repeat;
	clear: both;
	list-style: none;
	margin-bottom: 0.15em;
	margin-right: 8px;
	padding-bottom: 1px;
	padding-left: 25px;
}

.portlet-blogs ul.title-list {
	margin-left: 1em;
}