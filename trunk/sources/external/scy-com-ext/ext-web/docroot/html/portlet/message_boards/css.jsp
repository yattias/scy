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

.portlet-message-boards .code {
	background: #fff;
	border: 1px solid #777;
	font-family: monospace;
	overflow-x: auto;
	white-space: pre;
}

.ie6 .portlet-message-boards .code {
	width: 100%;
}

.portlet-message-boards .code-lines {
	border-right: 1px solid #ccc;
	color: #000;
	margin-right: 5px;
	padding: 0px 5px 0px 5px;
}

.portlet-message-boards .quote {
	background: #fff url(<%= themeImagesPath %>/message_boards/quoteleft.png) left top no-repeat;
	border: 1px solid #777;
	padding: 5px 0px 0px 5px;
}

.portlet-message-boards .quote-content {
	background: transparent url(<%= themeImagesPath %>/message_boards/quoteright.png) right bottom no-repeat;
	padding: 5px 30px 10px 30px;
}

.portlet-message-boards .quote-title {
	font-weight: bold;
	padding: 5px 0px 5px 0px;
}

.portlet-message-boards .title {
	border-bottom: 1px solid #ccc;
	font-size: large;
	font-weight: normal;
	padding: 5px;
}

.portlet-message-boards .message-container {
	border: 1px solid #ccc;
	margin: 5px 0 0 0;
}

.ie .portlet-message-boards .message-container {
	width: 100%;
}

.portlet-message-boards .message-container table {
	border-collapse: collapse;
	table-layout: fixed;
}

.portlet-message-boards .message-container td {
	border: none;
}

.portlet-message-boards .thread-top {
	border-bottom: 1px solid #ccc;
	padding: 3px 5px;
}

.portlet-message-boards .thread-bottom {
	padding: 3px 5px;
}

.portlet-message-boards .taglib-ratings.thumbs .total-rating {
	padding: 0 5px 0 10px;
}

td.user-info {
	border-right: 1px solid #ccc;
	width: 150px;
}

.portlet-message-boards .subject {
	float: left;
}

.portlet-message-boards .edit-controls {
	float: right;
}

.portlet-message-boards .edit-controls li {
	float: left;
	margin-right: 10px;
}

.portlet-message-boards .thread-body {
	padding: 15px;
}

.ie .portlet-message-boards .message-container .thread-body table {
	table-layout: auto;
}

.portlet-message-boards .message-container .user-info {
	border-right: 1px solid #ccc;
	padding: 5px;
}

.portlet-message-boards .clear {
	clear: both;
}

.portlet-message-boards .toggle_id_message_boards_view_message_thread {
	border: 1px solid #ccc;
	margin: 5px 0px 0px 0px;
}

.portlet-message-boards .thread-controls {
	border: 1px solid #ccc;
	margin-bottom: 5px;
	padding: 3px 5px;
}

.portlet-message-boards .thread-navigation {
	float: left;
}

.portlet-message-boards .thread-actions {
	float: right;
}

.portlet-message-boards .thread-user-rank {
	display: block;
}

.portlet-message-boards .emoticons {
	border: 1px solid #ccc;
	margin-left: 10px;
}

.portlet-message-boards .tree {
	vertical-align: middle;
}

.portlet-message-boards .message-scroll {
	margin: 5px 0px 0px 0px;
}

.portlet-message-boards .lfr-textarea.message-edit {
	height: 378px;
	min-height: 100%;
	width: 100%;
}

.portlet-message-boards .message-edit-body {
	width: 750px;
}

.portlet-message-boards .subcategories {
	text-decoration: underline;
}

.portlet-message-boards .taglib-flags {
	float: left;
	margin-left: 20px;
}


.portlet-message-boards .asset-abstract .asset-content p {
	margin-bottom: 0;
}

.portlet-message-boards .asset-content .asset-description {
	font-style: italic;
}

.portlet-message-boards .asset-content img {
	float: left;
	margin: 3px 10px 0 0;
}

.portlet-message-boards .asset-content {
	margin-bottom: 10px;
	margin-left: 25px;
	margin-right: 10px;
}

.portlet-message-boards .asset-full-content .asset-content {
	margin-right: 25px;
}

.portlet-message-boards .asset-full-content.show-asset-title .asset-content {
	margin-right: 10px;
}

.portlet-message-boards .asset-edit {
	text-align: right;
}

.portlet-message-boards .asset-metadata span {
	float: left;
}

.portlet-message-boards .asset-metadata {
	clear: both;
	margin-left: 25px;
}

.portlet-message-boards .asset-more {
	clear: left;
}

.portlet-message-boards .asset-tag-label {
	background-color: #EEE;
	clear: both;
	padding: 3px;
}

.portlet-message-boards .asset-title .asset-actions img {
	margin-left: 5px;
}

.portlet-message-boards .asset-title .asset-actions {
	float: right;
	font-size: 11px;
	font-weight: normal;
	margin-bottom: 3px;
	margin-top: 0;
}

.portlet-message-boards .asset-title a {
	text-decoration: none;
}

.portlet-message-boards .asset-title a:hover {
	text-decoration: underline;
}

.portlet-message-boards .asset-title {
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

.portlet-message-boards .blog {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/blogs.png);
}

.portlet-message-boards .bookmark {
	background-image: url(<%= themeImagesPath %>/ratings/star_hover.png);
}

.portlet-message-boards .content {
	background-image: url(<%= themeImagesPath %>/common/history.png);
}

.portlet-message-boards .dl-file-icon {
	margin: 0 5px 0 0;
}

.portlet-message-boards .document {
	background-image: url(<%= themeImagesPath %>/common/clip.png);
}

.portlet-message-boards .edit-controls {
	margin-bottom: 20px;
}

.portlet-message-boards .final-separator {
	border: 0;
	margin-bottom: 30px;
}

.portlet-message-boards .image {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/image_gallery.png);
}

.portlet-message-boards .lfr-meta-actions {
	padding-top: 0;
}

.portlet-message-boards .metadata-author {
	background: url(<%= themeImagesPath %>/portlet/edit_guest.png) no-repeat 0 0;
	float: left;
	font-weight: bold;
	margin-right: 10px;
	padding-left: 25px;
}

.portlet-message-boards .metadata-entry {
	color: #999;
	padding-bottom: 20px;
}

.portlet-message-boards .metadata-modified-date, .portlet-message-boards .metadata-create-date, .portlet-message-boards .metadata-publish-date, .portlet-message-boards .metadata-expiration-date {
	background: url(<%= themeImagesPath %>/common/date.png) no-repeat 0 0;
	color: #999;
	margin-bottom: 1em;
	padding-left: 25px;
}

.portlet-message-boards .metadata-priority {
	background: url(<%= themeImagesPath %>/common/top.png) no-repeat 0 20%;
	margin-right: 10px;
	padding-left: 25px;
}

.portlet-message-boards .metadata-view-count {
	margin-right: 10px;
}

.portlet-message-boards .separator {
	border-right: 1px solid #999;
	clear: both;
	margin: 25px 25px;
}

.portlet-message-boards .taglib-tags-summary {
	float: left;
}

.portlet-message-boards .thread {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/message_boards.png);
}

.portlet-message-boards .title-list .asset-actions {
	left: 10px;
	position: relative;
}

.portlet-message-boards .title-list .asset-metadata {
	padding: 0;
}

.portlet-message-boards .title-list a {
	float: left;
}

.portlet-message-boards .vertical-separator {
	border-right: 1px solid #999;
	float: left;
	margin: 0 10px;
	padding: 7px 0;
}

.portlet-message-boards .wiki {
	background-image: url(<%= themeImagesPath %>/common/pages.png);
}

.portlet-message-boards li.title-list {
	background-position: 0 0;
	background-repeat: no-repeat;
	clear: both;
	list-style: none;
	margin-bottom: 0.15em;
	margin-right: 8px;
	padding-bottom: 1px;
	padding-left: 25px;
}

.portlet-message-boards ul.title-list {
	margin-left: 1em;
}