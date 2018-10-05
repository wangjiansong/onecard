<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

 // properties 配置文件名称
    ResourceBundle res = ResourceBundle.getBundle("sysConfig");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%=res.getString("LibName")%>在线实名注册使用协议</title>
<link href="/onecard/media/css/register/css/agreement.css" rel="stylesheet" type="text/css" />
</head>
<body>
<div class="container">
<h2>《<%=res.getString("LibName")%>在线实名注册使用协议》</h2>
<hr>
<h4>1、总则</h4>
<p>1.1本协议中的<%=res.getString("LibName")%>网站（以下简称网站）是指<%=res.getString("LibName")%>主网站(<%=res.getString("IpAddess")%>)及其子域名网站。网站所提供的公益服务由<%=res.getString("LibName")%>提供（以下简称服务提供方）。</p><p>
1.2此协议适用于规范本网站和实名注册的用户（以下简称：读者）之间的行为和关系，以保护读者和网站双方的合法权益。</p><p>
1.3您要成为<%=res.getString("LibName")%>在线实名注册读者，在注册过程中，根据提示可以选择“同意”的操作，当点选“同意”按钮时即视为您已仔细阅读本条款的所有内容，同意接受本协议条款的所有规范包括接受本网站根据服务需要对本协议条款随时所做的任何修改，并愿意受其约束。</p><p>
1.4如果您对本协议的任何条款或者将来随时可能修改、补充的条款有异议，您可选择“不同意”按钮，不注册成为本网站的用户。</p><p>
1.5 本协议条款内容变更或补充，将随时在网站上发布通知、公告、声明或其它类似内容，<%=res.getString("LibName")%>网站不承担通知到个人的义务，读者在享受各项服务时应当及时关注协议和服务的变化情况。</p><p>
1.6 本协议适用于网站为读者提供的各种服务。当读者使用网站某一特定服务时，如该服务另有单独的服务条款、指引或规则，读者应同时遵守本协议条款和该服务另行约定的相关服务条款、指引或规则等。</p><p>
1.7 本系统所提供的服务将完全按照其发布的章程、服务条款和操作规则严格执行。读者选择同意所有注册条款并完成注册程序，即能成为网站的正式用户。 </p>
<h4>2、服务内容</h4>
<p>2.1 <%=res.getString("LibName")%>网站将为在线实名注册成功的读者提供包括但不限于文献检索、在线数字资源访问等各种在线服务,具体服务内容由网站根据实际情况进行提供，并在提供服务时，另行约定服务条款、指引或规则。</p><p>
2.2 <%=res.getString("LibName")%>网站仅提供相关的网络服务，除此之外与相关网络服务有关的设备(如个人电脑、手机、及其他与接入互联网或移动网有关的装置)及所需的费用(如为接入互联网而支付的电话费及上网费、为使用移动网而支付的手机费)均应由用户自行负担。 
</p><h4>3、信息注册</h4>
<p>3.1读者在注册时必须提供真实、完整及准确的个人资料，并及时更新。用户名的注册与使用应符合网络道德，遵守中华人民共和国的相关法律法规。</p><p>
3.2 <%=res.getString("LibName")%>统一用户管理系统将读者注册时提交的信息进行核验，完成读者实名注册。<%=res.getString("LibName")%>网站有权对读者（含已实名验证和未实名验证）的权限进行设定和调整。本网站不接受同一身份证或手机号码重复注册。</p><p>
3.3读者注册成功后，须保护好自己的帐号信息，因读者本人泄露而造成的任何损失由用户自行承担。</p><p>
3.4 用户帐号的所有权归<%=res.getString("LibName")%>网站，用户仅享有使用权。</p><p>
3.5读者通过本网站得到并使用的网络服务不能作其他非法用途。</p> 
<h4>4、使用规则</h4>
<p>
4.1 遵守中华人民共和国相关法律法规，包括但不限于《中华人民共和国网络安全法》、《中华人民共和国计算机信息系统安全保护条例》、《计算机软件保护条例》、《最高人民法院关于审理涉及计算机网络著作权纠纷案件适用法律若干问题的解释(法释[2004]1号)》、《全国人大常委会关于维护互联网安全的决定》、《互联网电子公告服务管理规定》、《互联网新闻信息服务管理规定》、《互联网著作权行政保护办法》和《信息网络传播权保护条例》等有关计算机互联网规定和知识产权的法律和法规、实施办法。</p><p>
4.2 读者对其自行发表、上传或传送的内容负全部责任，所有用户不得在<%=res.getString("LibName")%>网站任何页面发布、转载、传送含有下列内容之一的信息，否则<%=res.getString("LibName")%>网站有权自行处理并不通知用户： (1)违反宪法确定的基本原则的； (2)危害国家安全，泄漏国家机密，颠覆国家政权，破坏国家统一的； (3)损害国家荣誉和利益的； (4)煽动民族仇恨、民族歧视，破坏民族团结的； (5)破坏国家宗教政策，宣扬邪教和封建迷信的； (6)散布谣言，扰乱社会秩序，破坏社会稳定的； (7)散布淫秽、色情、赌博、暴力、恐怖或者教唆犯罪的； (8)侮辱或者诽谤他人，侵害他人合法权益的； (9)煽动非法集会、结社、游行、示威、聚众扰乱社会秩序的； (10)以非法民间组织名义活动的； (11)含有法律、行政法规禁止的其他内容的。</p><p>
4.3 当第三方认为读者在网站上发表或上传信息侵犯其权利，并根据《信息网络传播权保护条例》或者相关法律规定向网站发送权利通知书时，读者同意网站可以自行判断决定删除涉嫌侵权信息，除非读者提交书面证据材料排除侵权的可能性，网站将不会恢复上述删除的信息。</p><p>
4.4 如读者在使用网络服务时违反上述任何规定，网站有权要求读者改正或直接采取一切必要的措施(包括但不限于删除用户张贴的内容、暂停或终止用户使用网络服务的权利)以减轻读者不当行为而造成的影响。</p><p>
4.5 读者随时可以根据网站提供的功能对个人信息进行维护。</p><p>
4.6网站不对读者所发布信息的删除或储存失败负责。</p><p>
4.7读者在享受服务时不应干扰或扰乱网络服务，不得盗用他人帐号信息，须遵守所有使用网络服务的网络协议、规定、程序和惯例。</p><p>
4.8读者若在网站上散布和传播反动、色情或其他违反国家法律的信息，网站的系统记录有可能作为用户违反法律的证据，并终止其在本网站的相关服务。</p><p>
4.9读者的授权行为：对网站而言，只要使用了正确的用户账号和密码信息，无论是谁登陆均视为已经得到注册读者本人的授权。 
</p><h4>5、服务暂停、变更与中止条款</h4>
<p>
5.1鉴于网络服务的特殊性，网站有权因需要随时变更、中断或终止部分或全部的网络服务。</p><p>
5.2 网站有权判定读者的行为是否符合服务提供方注册条款、免责条款，如果读者违背了注册条款和免责条款的规定，则有权中断读者的服务。</p><p>
5.3由于网站服务需要定期或不定期地对提供网络服务的平台或相关的设备进行检修或者维护，如因此类情况而造成网络服务在合理时间内的中断，网站无需为此承担任何责任，但应尽可能事先进行通告。</p><p>
5.4如发生下列任何一种情形，网站有权随时中断或终止向读者提供本协议项下的网络服务而无需对读者或任何第三方承担任何责任： (1) 读者提供的个人资料不真实； (2) 读者违反本协议中规定的使用规则； (3) 网站服务提供方认为其他不适宜的地方。</p><p>
5.5帐号或身份信息被其他人冒用或盗用时，读者可通过网站提供的申诉途径与网站取得联系，提交相关材料，并经网站核实后修正个人资料，恢复账号的正确使用。</p><p>
5.6 读者对后来的条款修改有异议，或对服务提供方的服务不满，可以行使如下权利： （1）停止使用网站提供的网络服务。 （2）告知网站停止对自己服务。 服务结束后，读者使用网络服务的权利马上终止，网站服务提供方不对读者承担任何义务和责任。 
</p><h4>6、读者隐私制度及保护</h4>
<p>
6.1<%=res.getString("LibName")%>网站承诺对读者资料实行保密，未经合法用户授权时，保证不对外公开或向第三方提供其注册资料，但以下情况除外： (1)事先获得读者的明确授权； (2)根据法律有关规定，或者行政、司法机构的要求，向第三方或者行政、司法机构披露； (3)按照相关政府主管部门的要求； (4)为维护社会公众的利益； (5)如果读者出现违反中国有关法律或者网站政策的情况，需要向第三方披露； (6)为提供读者所要求的产品和服务，而必须和第三方分享读者的个人信息； (7)不可抗力所导致的读者信息公开； (8)由于本系统硬件和软件的能力限制，所导致读者信息的公开； (9) 如有符合资格的知识产权投诉人并已提起投诉，应被投诉人要求，向被投诉人披露，以便双方处理可能的权利纠纷；（10）因通知等服务需要，需向短信运营商提供读者手机号等基本信息（若不同意，请不接受此协议）。</p><p>
6.2 在不泄露读者隐私资料的前提下，<%=res.getString("LibName")%>网站有权对整个读者数据库进行以研究或运行监控等目的的统计与分析。 
</p><h4>7、读者帐号、密码和安全性</h4>
<p>
7.1读者一旦注册成功，即为网站的合法用户，读者权限（用户信息）只允许用户本人使用，读者不得将其帐号信息转让、出借或给予他人使用。</p><p>
7.2 每个读者都要对其以帐号信息进行的所有活动和事件负全责。</p><p>
7.3 读者若发现任何非法使用用户帐号或存在安全漏洞的情况，请立即通知服务提供方。因黑客行为或读者的保管疏忽等情况导致帐号信息遭他人非法使用，网站不承担责任。 
</p><h4>8、版权声明</h4>
<p>
8.1 <%=res.getString("LibName")%>网站的文字、图片、音频、视频等版权均归<%=res.getString("LibName")%>网站享有或<%=res.getString("LibName")%>网站与作者共同享有，未经<%=res.getString("LibName")%>网站许可，不得任意转载。</p><p>
8.2 <%=res.getString("LibName")%>网站特有的标识、版面设计、编排方式等版权均属<%=res.getString("LibName")%>网站享有，未经许可，不得任意复制或转载。</p><p>
8.3 使用<%=res.getString("LibName")%>网站的任何内容均应注明“来源于<%=res.getString("LibName")%>(<%=res.getString("IpAddess")%>)”及署上作者姓名，按法律规定需要支付稿酬的，应当通知<%=res.getString("LibName")%>网站及作者及支付稿酬，并独立承担一切法律责任。</p><p>
8.4 <%=res.getString("LibName")%>网站享有所有作品用于其它用途的优先权，包括但不限于网站、电子杂志、平面出版等，但在使用前会通知作者，并按同行业的标准支付稿酬。</p><p>
8.5 恶意转载<%=res.getString("LibName")%>网站内容的，<%=res.getString("LibName")%>保留将其诉诸法律的权利。 
</p><h4>9、有限责任</h4>
<p>
9.1 读者明确同意其使用<%=res.getString("LibName")%>网站网络服务所存在的风险及一切后果将完全由用户本人承担，网站和服务提供方对此不承担任何责任。</p><p>
9.2 服务提供方无法保证网络服务一定能满足读者的要求，也不保证网络服务的及时性、安全性、准确性。</p><p>
9.3 对于因不可抗力或<%=res.getString("LibName")%>不能控制的原因造成的网络服务中断或其它缺陷，服务提供方不承担任何责任，但将尽力减少因此而给读者造成的损失和影响。</p><p>
9.4 对于<%=res.getString("LibName")%>网站向读者提供的下列产品或者服务的质量缺陷本身及其引发的任何损失，服务提供方无需承担任何责任： (1)<%=res.getString("LibName")%>网站向读者免费提供的各项网络服务； (2)<%=res.getString("LibName")%>网站向读者赠送的任何产品或者服务；</p><p>
9.5 <%=res.getString("LibName")%>网站有权于任何时间暂时或永久修改或终止本服务(或其任何部分)，而无论其通知与否，服务提供方对读者和任何第三人均无需承担任何责任。 
</p><h4>10、信息通告</h4>
<p>
10.1 本协议项下所有的通知均可通过重要页面公告、电子邮件或常规的信件传送等方式进行；该通知于发送之日视为已送达。</p><p>
10.2 服务提供方可通过网站对外正式公布读者通告。 
</p><h4>11、法律适用及争议解决</h4>
<p>
本服务条款之效力和解释均适用中华人民共和国之法律。如服务条款之任何一部分与中华人民共和国法律相抵触，则该部分条款应按法律规定重新解释，部分条款之无效或重新解释不影响其它条款之法律效力。读者和服务提供方一致同意凡因本网站服务所产生的纠纷双方应协商解决，协商不成任何一方可提交服务提供方所在地法院诉讼裁决。 
</p><h4>12、附则</h4>
<p>
12.1 本协议的订立、执行和解释及争议的解决均应适用中华人民共和国法律。</p><p>
12.2 如本协议中的任何条款无论因何种原因完全或部分无效或不具有执行力，本协议的其余条款仍应有效并且有约束力。</p><p>
12.3 本协议解释权及修订权归<%=res.getString("LibName")%>网站所有。</p><p>
12.4 <%=res.getString("LibName")%>既是<%=res.getString("LibName")%>网站服务的提供方，也是承担网站各项权利和义务的法人单位。
</p>
<hr>
<p class="footer"> &copy; <%=res.getString("LibName")%></p>
</div>
</body>
</html>