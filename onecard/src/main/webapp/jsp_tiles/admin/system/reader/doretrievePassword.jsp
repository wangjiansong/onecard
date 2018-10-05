<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>密码找回页面</title>
        <script type="text/javascript">
             function search(){
                 if(document.form1.rdId.value==""||document.form1.rdId.value==null){
                  window.alert("读者证号不能为空！"); 
                  return false;
                 }
                 if(document.form1.rdCertify.value==""||document.form1.rdCertify.value==null){
                  window.alert("身份证号不能为空！"); 
                  return false;
                 }
                  if(document.form1.newPassword.value==""||document.form1.newPassword.value==null){
                  window.alert("新密码不能为空！"); 
                  return false;
                 }
                 document.getElementById("form1").action="selectPassword";
                 document.getElementById("form1").submit();
             }
           
                //                奇偶行 颜色不同
                $(".table_b tr").each(function(n){
                    if(n%2==1){
                        this.className="tb2";
                        $(this).find("td").each(function(m){
                            this.className="tb2";
                        });
                    }else{
                        this.className="tb3";
                        $(this).find("td").each(function(m){
                            this.className="tb3";
                        })
                    }
                });
                
        </script>
    </head>
    <body>
        <form name="form1" id="form1" method="get">
            <table  width="962" valign="top"  height="100%"  border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                    <td align="center" valign="top">
                        <table class="im_table" style="margin: 0px auto"  width="100%" height="100%" cellpadding="0" cellspacing="0">
                            <tr>
                                <td height="30" width="35%"><div align="right">读者证号：</div></td>
                                <td colspan="2"><div align="left"><input name="rdId" type="text" id="rdId" size="20" /></div></td>
                            </tr>
                            <tr>
                                <td height="30" width="35%"><div align="right">身份证号：</div></td>
                                <td colspan="2"><div align="left"><input name="rdCertify"   type="text" id="rdCertify" size="20" /></div></td>
                            </tr>
                            <!-- <tr>
                                <td height="30" width="35%"><div align="right">旧密码：</div></td>
                                <td colspan="2">
                                    <div align="left">
                                        <input name="oldRdpasswd"  type="text" id="oldRdpasswd"  />
                                    </div>
                                </td>
                            </tr> -->
                            <tr>
                                <td height="30" width="35%"><div align="right">新密码：</div></td>
                                <td colspan="2">
                                    <div align="left">
                                        <input name="newPassword"  type="text" id="newPassword"  />
                                    </div>
                                </td>
                            </tr>
                            <!-- <tr>
                                <td height="30" width="35%"><div align="right">确认新密码：</div></td>
                                <td colspan="2">
                                    <div align="left">
                                        <input name="rdPasswd"  type="text" id="rdPasswd"  />
                                    </div>
                                </td>
                            </tr> -->
                            <tr>
                                <td height="30" colspan="3">
                                    <span style="COLOR: #880000">
                                        <div align="center">
                                            <input type="button" name="button1" id="button1" class="im_midbg" value="确认修改" onclick="search();"/>&nbsp;&nbsp;
                                         
                                            <input type="button" name="button2" id="button2" class="im_midbg" value="返回登录" onclick="window.location.href='login';"/>
                                        </div>
                                    </span>
                                </td>
                            </tr>  
                            <tr>
                                <td class="message" height="5" colspan="3">
                                    <div id="div" align="center">
                                        <font color="red"></font>
                                    </div>
                                </td>
                            </tr>
                         </table>
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>