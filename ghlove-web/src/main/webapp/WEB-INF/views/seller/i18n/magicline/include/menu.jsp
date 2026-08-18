<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8"%>
<table id="main-table" border="0" cellspacing="0">
	<tr>
		<td id="header" colspan="2">
		<div id="header-div">
		<div class="right-logo">MagicLine4Web Console</div>
		<div class="left-logo"><a href="index.jsp" class="header-home">
			<img src="./images/1px.gif" width="300px" height="32px" /></a>
		</div>
		<div class="middle-ad"></div>
		<div class="header-links">
			<div class="right-links">
			<ul>

			</ul>
			</div>
		</div>
		</div>
		</td>
	</tr>
	<tr>
		<td id="menu-panel" valign="top">
		<table id="menu-table" style="border:0;border-spacing:0;">
			<tr>
				<td id="magicline">
					<div id="menu">
						<ul class="main">
							<li id="magicline_v40_menu" class="menu-header"
								onclick="mainMenuCollapse(this.childNodes[0])"
								style="cursor: pointer"><img src="./images/up-arrow.gif"
								class="mMenuHeaders" id="magicline_v40_menu" />[Samples]MagicLine4Web
							</li>


							<li class="menu-disabled-link">전자서명</li>
							<li class="normal">
								<ul class="sub">
									<li><a href="/opmanager/magicline/signedForm" class="menu-default">Form 전자서명 </a></li>

									<li><a href="../ML4WebExample/addSignedForm.jsp" class="menu-default">AddSign </a></li>


								<!-- 	<li><a href="../ML4WebExample/signEncrypt.jsp" class="menu-default" style="background-image: url(../endpoints/images/endpoints-icon.gif);">전자서명 & 암호화</a></li> -->
								</ul>
							</li>
							<li class="menu-disabled-link">본인확인 & 전자서명</li>
							<li class="normal">
								<ul class="sub">
									<li><a href="../ML4WebExample/vidClientIDN.jsp" class="menu-default">서버에서 검증 - IDN 입력 </a></li>
									<!-- <li><a href="../ML4WebExample/vidServerIDN.jsp" class="menu-default">서버에서 검증 - 서버 IDN 이용  </a></li> -->
								</ul>
							</li>




						</ul>
					</div>
				</td>
			</tr>
			<tr>
				<td><img src="./images/1px.gif" width="225px" height="1px" /></td>
			</tr>
		</table>
		</td>
		<td id="middle-content">
		<table id="content-table" style="border:0;border-spacing:0;">
			<tr>
				<td id="page-header-links">
				<table class="page-header-links-table" style="border-spacing:0;">
					<tr>
						<td class="breadcrumbs">
						<table class="breadcrumb-table" style="border-spacing:0;">
							<tr>
								<td>
								<div id="breadcrumb-div"></div>
								</td>
							</tr>

						</table>
						</td>

						<!-- <td class="page-header-help"><a href="./docs/userguide.html"
							target="_blank"></a></td> -->
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td id="body"><img src="./images/1px.gif" width="735px" height="1px" />
