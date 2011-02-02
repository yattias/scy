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

.portlet-wiki .wiki-body pre {
	background : #fff;
	border: 1px dashed #2f6fab;
	margin: 5px 0px 5px 0px;
	padding: 0.5em;
}

.portlet-wiki a.createpage {
	color: #f00;
}

.portlet-wiki .toc {
	border: 1px solid #aaa;
	background-color: #f9f9f9;
	padding: 10px;
}

.portlet-wiki .toc h4 {
	margin-bottom: 0.7em;
}

.portlet-wiki .toc ul {
 	margin-top: 0px;
}
.portlet-wiki .toc li.toclevel-1 {
	list-style-type: none;
	margin-left: 0px;
}

.portlet-wiki .toc li.toclevel-2 {
	list-style-type: none;
	margin-left: 15px;
}

.portlet-wiki .toc li.toclevel-3 {
	list-style-type: none;
	margin-left: 30px;
}

.portlet-wiki .taglib-discussion {
	margin-top: 18px;
}

.portlet-wiki .taglib-tags-summary {
	margin: -10px 0px 10px 0px;
	color: #7d7d7d;
}

.portlet-wiki h1.page-title {
	border-bottom: 1px solid #AAAAAA;
	margin: 0px;
	margin-bottom: 0.5em;
	padding-bottom: 5px;
}

.portlet-wiki h1.page-title .return-to-page {
	background: url(<%= themeImagesPath %>/wiki/return_to_page.png) no-repeat 0 50%;
	padding-left: 20px;
	text-decoration: none;
}

.portlet-wiki .preview {
	background: #ffc;
	border: 1px dotted gray;
	padding: 3px;
}

.portlet-wiki .child-pages h3 {
	font-size: 1.2em;
	margin-bottom: 0.3em;
}

.ie .portlet-wiki .child-pages h3 {
	margin-bottom: 0.2em;
}

.portlet-wiki .child-pages ul {
	margin-top: 0;
}

.portlet-wiki .child-pages li {
	font-weight: bold;
	font-size: 1.1em;
}

.portlet-wiki .content-body .wiki-code {
	background: #fff;
	border: 1px solid #777;
	font-family: monospace;
	white-space: pre;
}

.portlet-wiki .content-body .code-lines {
	border-right: 1px solid #ccc;
	color: #000;
	margin-right: 5px;
	padding: 0px 5px 0px 5px;
}

.portlet-wiki .content-body a.external-link {
	background: transparent url(<%= themeImagesPath %>/wiki/external.png) right top no-repeat;
	text-decoration: none;
	padding-right: 10px;
}

.portlet-wiki .content-body a.external-link:hover {
	background: transparent url(<%= themeImagesPath %>/wiki/external.png) right top no-repeat;
	text-decoration: underline;
	padding-right: 11px;
}

.portlet-wiki .node-current {
	text-decoration: none;
	font-weight: bold;
}

.portlet-wiki .page-actions {
	margin-top: 1.5em;
}

.portlet-wiki .page-actions .article-actions {
	border-right: 1px solid #999;
	float: left;
	margin-right: 10px;
	padding-right: 10px;
}

.portlet-wiki .page-actions .stats {
	color: #999;
}

.portlet-wiki .page-title .page-actions {
	float: right;
	margin-top: 0;
}

.portlet-wiki .page-title .page-actions a {
	text-decoration: none;
}

.portlet-wiki .page-actions a:hover {
	text-decoration: underline;
}

.portlet-wiki .page-title .page-actions {
	font-size: 11px;
	font-weight: normal;
}

.portlet-wiki .page-title .page-actions img {
	margin-left: 5px;
}

.portlet-wiki .page-info {
	width: 100%;
}

.portlet-wiki .page-info tr th, .portlet-wiki .page-info tr td {
	border: 1px solid #ccc;
	border-left: none;
	border-right: none;
	padding: 0.3em 1em;
}

.portlet-wiki .page-old-version a {
	color: #ff9933;
}

.portlet-wiki .page-old-version {
	color: #ff9933;
	line-height: 1.2em;
	margin: -1em 0pt 1.4em 0em;
	width: auto;
}

.portlet-wiki .page-redirect {
	color: #7d7d7d;
	cursor: pointer;
	line-height: 1.2em;
	margin: -1em 0pt 1.4em 0em;
	width: auto;
}

.portlet-wiki .page-redirect:hover {
	text-decoration: underline;
}

.portlet-wiki .popup-print {
	float: right;
}

.portlet-wiki .syntax-help {
	border: 1px dotted gray;
	padding-left: 10px;
}

.portlet-wiki .syntax-help h4 {
	margin-bottom: 0.5em;
}
.ie .portlet-wiki .syntax-help h4 {
	margin-bottom: 0.3em;
}

.portlet-wiki .syntax-help pre {
	margin-left: 1em;
	margin-bottom: 1em;
}

.portlet-wiki .top-links {
	padding-bottom: 10px;
}

.portlet-wiki .top-links table {
	width: 100%;
}

.portlet-wiki .subscription-info tr td {
	border: none;
	padding: 0.1em 10px 0.1em 0;
}

.portlet-wiki .asset-abstract .asset-content p {
	margin-bottom: 0;
}

.portlet-wiki .asset-content .asset-description {
	font-style: italic;
}

.portlet-wiki .asset-content img {
	float: left;
	margin: 3px 10px 0 0;
}

.portlet-wiki .asset-content {
	margin-bottom: 10px;
	margin-left: 25px;
	margin-right: 10px;
}

.portlet-wiki .asset-full-content .asset-content {
	margin-right: 25px;
}

.portlet-wiki .asset-full-content.show-asset-title .asset-content {
	margin-right: 10px;
}

.portlet-wiki .asset-edit {
	text-align: right;
}

.portlet-wiki .asset-metadata span {
	float: left;
}

.portlet-wiki .asset-metadata {
	clear: both;
	margin-left: 25px;
}

.portlet-wiki .asset-more {
	clear: left;
}

.portlet-wiki .asset-tag-label {
	background-color: #EEE;
	clear: both;
	padding: 3px;
}

.portlet-wiki .asset-title .asset-actions img {
	margin-left: 5px;
}

.portlet-wiki .asset-title .asset-actions {
	float: right;
	font-size: 11px;
	font-weight: normal;
	margin-bottom: 3px;
	margin-top: 0;
}

.portlet-wiki .asset-title a {
	text-decoration: none;
}

.portlet-wiki .asset-title a:hover {
	text-decoration: underline;
}

.portlet-wiki .asset-title {
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

.portlet-wiki .blog {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/blogs.png);
}

.portlet-wiki .bookmark {
	background-image: url(<%= themeImagesPath %>/ratings/star_hover.png);
}

.portlet-wiki .content {
	background-image: url(<%= themeImagesPath %>/common/history.png);
}

.portlet-wiki .dl-file-icon {
	margin: 0 5px 0 0;
}

.portlet-wiki .document {
	background-image: url(<%= themeImagesPath %>/common/clip.png);
}

.portlet-wiki .edit-controls {
	margin-bottom: 20px;
}

.portlet-wiki .final-separator {
	border: 0;
	margin-bottom: 30px;
}

.portlet-wiki .image {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/image_gallery.png);
}

.portlet-wiki .lfr-meta-actions {
	padding-top: 0;
}

.portlet-wiki .metadata-author {
	background: url(<%= themeImagesPath %>/portlet/edit_guest.png) no-repeat 0 0;
	float: left;
	font-weight: bold;
	margin-right: 10px;
	padding-left: 25px;
}

.portlet-wiki .metadata-entry {
	color: #999;
	padding-bottom: 20px;
}

.portlet-wiki .metadata-modified-date, .portlet-wiki .metadata-create-date, .portlet-wiki .metadata-publish-date, .portlet-wiki .metadata-expiration-date {
	background: url(<%= themeImagesPath %>/common/date.png) no-repeat 0 0;
	color: #999;
	margin-bottom: 1em;
	padding-left: 25px;
}

.portlet-wiki .metadata-priority {
	background: url(<%= themeImagesPath %>/common/top.png) no-repeat 0 20%;
	margin-right: 10px;
	padding-left: 25px;
}

.portlet-wiki .metadata-view-count {
	margin-right: 10px;
}

.portlet-wiki .separator {
	border-right: 1px solid #999;
	clear: both;
	margin: 25px 25px;
}

.portlet-wiki .taglib-tags-summary {
	float: left;
}

.portlet-wiki .thread {
	background-image: url(<%= PortalUtil.getPathContext() %>/html/icons/message_boards.png);
}

.portlet-wiki .title-list .asset-actions {
	left: 10px;
	position: relative;
}

.portlet-wiki .title-list .asset-metadata {
	padding: 0;
}

.portlet-wiki .title-list a {
	float: left;
}

.portlet-wiki .vertical-separator {
	border-right: 1px solid #999;
	float: left;
	margin: 0 10px;
	padding: 7px 0;
}

.portlet-wiki .wiki {
	background-image: url(<%= themeImagesPath %>/common/pages.png);
}

.portlet-wiki li.title-list {
	background-position: 0 0;
	background-repeat: no-repeat;
	clear: both;
	list-style: none;
	margin-bottom: 0.15em;
	margin-right: 8px;
	padding-bottom: 1px;
	padding-left: 25px;
}

.portlet-wiki ul.title-list {
	margin-left: 1em;
}