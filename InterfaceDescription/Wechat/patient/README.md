



 ## 接口列表

|  接口  | 说明 |
|------ |----- |
|[https://cdn.cmas2020.cn/wechat/patient/getIdentifyCode](#first)| 获取验证码|
|[https://cdn.cmas2020.cn/wechat/patient/login/loginWithIdentifyCode](#second)| 通过验证码登录|
|[https://cdn.cmas2020.cn/wechat/patient/login/loginWithPasswd](#third)|通过密码登录|
|[https://cdn.cmas2020.cn/wechat/patient/register/registerWithPasswd](#four)| 用户注册|
|[https://cdn.cmas2020.cn/wechat/patient/register/modifyPasswd](#five)| 用户修改密码|
|[https://cdn.cmas2020.cn/wechat/patient/user/setUserInfo](#six)| 用户填写和修改个人信息|
|[https://cdn.cmas2020.cn/wechat/patient/user/getUserInfo](#seven)| 返回个人信息|
|[http://cdn-test.cmas2020.cn/wechat/patient/getOpendId](#seven)| 登录时先获取openId|


***


## 接口详情
* <span id = "first">获取验证码</span>

    * 接口地址：https://cdn.cmas2020.cn/wechat/patient/getIdentifyCode

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：用户在通过验证码登录，注册，修改密码的时候都需要获取验证码

    * 请求参数说明：

        | 名称 | 类型  |说明|
        |----- |------| ----|
        |tel|String|电话|
        |type | Int| 获取验证码的用途（0登录，1注册，2修改密码）|
       

    * 返回参数说明：

        | 名称 | 类型 |说明|
        |----- |------|----|
        | errorCode| int|错误码:200;502
        |errorMsg| String|内容：发送成功;发送失败
        
---

* <span id = "second">通过验证码登录</span>

    * 接口地址：https://cdn.cmas2020.cn/wechat/patient/login/loginWithIdentifyCode

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：直接通过验证码登录

    * 请求参数说明：

       | 名称 | 类型  |说明|
        |----- |------| ----|
        |tel|String|电话|
        |code| String| 验证码|

    * 返回参数说明：

         | 名称 | 类型 |说明|
        |----- |------|----|
        | errorCode| int|错误码:200;504;505
        |errorMsg| String|内容：登录成功;用户不存在;验证码错误或已失效
        



---
* <span id = "third">通过密码登录</span>

    * 接口地址：https://cdn.cmas2020.cn/wechat/patient/login/loginWithPasswd

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：通过密码登录

    * 请求参数说明：

       | 名称 | 类型  |说明|
        |----- |------| ----|
        |tel|String|电话|
        |password| String| 经md5加密过的密码|

    * 返回参数说明：

         | 名称 | 类型 |说明|
        |----- |------|----|
        | errorCode| int|错误码:200;401
        |errorMsg| String|内容：登录成功;密码错误或账户不存在
        



---
* <span id = "four">用户注册</span>

    * 接口地址：https://cdn.cmas2020.cn/wechat/patient/register/registerWithPasswd

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：用户注册，需要输入电话，密码和验证码

    * 请求参数说明：

       | 名称 | 类型  |说明|
        |----- |------| ----|
        |tel|String|电话|
        |password| String|经md5加密过的密码 |
        |code| String|验证码 |

    * 返回参数说明：

         | 名称 | 类型 |说明|
        |----- |------|----|
        | errorCode| int|错误码:200;501;503;505;506
        |errorMsg| String|内容：注册成功;验证码不正确;验证码不存在或已过期;该手机号已注册;密码不能为空
        



---
* <span id = "five">用户修改密码</span>

    * 接口地址：https://cdn.cmas2020.cn/wechat/patient/register/modifyPasswd

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：修改密码，需要输入验证码进行修改

    * 请求参数说明：

       | 名称 | 类型  |说明|
        |----- |------| ----|
        |tel|String|电话|
        |password| String|经md5加密过的密码 |
        |code| String|验证码 |

    * 返回参数说明：

         | 名称 | 类型 |说明|
        |----- |------|----|
        | errorCode| int|错误码:200;505;506
        |errorMsg| String|内容：修改成功;用户不存在;验证码错误或已失效
        
---
* <span id = "six">用户填写或修改个人信息</span>

    * 接口地址：https://cdn.cmas2020.cn/wechat/patient/user/setUserInfo

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：用户填写个人信息

    * 请求参数说明：

       | 名称 | 类型  |说明|
       |----- |------| ----|
       |tel|String|电话|
       |userName| String|昵称 |
       |img| byte[]|图片的base64编码 |
       |gender|Int|性别（0男1女）|
       |birth|String|出生日期|
       |occupation|String|职业大类（房地产建筑，IT互联网，文化传媒等）|
       |occupationType|String|职业小类（房地产建筑中的 行政人事）|
       |isStudent|Int|是否为在校生（0否1是）|
       |university|String|学校|
       |college|String|学院|
       |major|String|专业|
       |userType|Int|用户类型（0普通用户，1心理精神障碍困扰用户）|
       |diseaseType|Int|疾病类型（0抑郁症，1双相障碍，2焦虑症，3睡眠障碍，4其他）|
       |psychoStauts|Int|状态（0住院病人, 1门诊病人, 2未治疗，3康复病人）|
       |hospital|Int|医院（0武大人民医院，1其他）|
       |therapist|String|医生|
       |department|String|科室|
      


    * 返回参数说明：

        | 名称 | 类型 |说明|
        |----- |------|----|
        | errorCode| int|错误码:200;505
        |errorMsg| String|内容：修改成功;用户不存在
---

* <span id = "seven">返回用户个人信息</span>

    * 接口地址：https://cdn.cmas2020.cn/wechat/patient/user/getUserInfo

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：返回用户个人信息

    * 请求参数说明：

       | 名称 | 类型  |说明|
       |----- |------| ----|
       |tel|String|电话|

 	* 返回参数说明：

       | 名称 | 类型  |说明|
       |----- |------| ----|
       | errorCode| int|错误码:200;505
       |errorMsg| String|内容：查询成功;用户不存在
       |tel|String|电话|
       |information|object|information包含下列若干信息
       |userName| String|昵称 |
       |img| byte[]|图片的base64编码 |
       |gender|Int|性别（0男1女）|
       |birth|String|出生日期|
       |occupation|String|职业大类（房地产建筑，IT互联网，文化传媒等）|
       |occupationType|String|职业小类（房地产建筑中的 行政人事）|
       |isStudent|Int|是否为在校生（0否1是）|
       |university|String|学校|
       |college|String|学院|
       |major|String|专业|
       |userType|Int|用户类型（0普通用户，1心理精神障碍困扰用户）|
       |diseaseType|Int|疾病类型（0抑郁症，1双相障碍，2焦虑症，3睡眠障碍，4其他）|
       |psychoStauts|Int|状态（0住院病人, 1门诊病人, 2未治疗，3康复病人）|
       |hospital|Int|医院（0武大人民医院，1其他）|
       |therapist|String|医生|
       |department|String|科室|


 返回参数举例
        
        ```json
       {
    "errorCode": 200,
    "errorMsg": "查询成功",
    "information": {
        "tel": "17649872470",
        "userName": null,
        "img": null,
        "gender": 1,
        "birth": "1998-01-10",
        "occupation": "IT",
        "occupationType": "后端工程师",
        "isStudent": 1,
        "university": "华科",
        "college": "计算机",
        "major": null,
        "userType": 1,
        "diseaseType": 1,
        "psychoStatus": 1,
        "hospital": 1,
        "department": "",
        "therapist": ""
    }

        
---
* <span id = "five">登录时先获取openId</span>

    * 接口地址：http://cdn-test.cmas2020.cn/wechat/patient/getOpendId

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：在授权手机号登录时先要调用wx.login()获取code，然后来后台换取openId和session_key

    * 请求参数说明：

        | 名称 | 类型  |说明|
        |----- |------| ----|
        |code|String|wx.login()获取的code|
        

    * 返回参数说明：

        | 名称 | 类型 |说明|
        |----- |------|----|
        |openid |String|通过微信服务器换取的openId|
        | errorCode| int|错误码:200;502|
        |errorMsg| String|内容：获取成功,获取失败|
        
---

* <span id = "five">手机号一键登录</span>

    * 接口地址：http://cdn-test.cmas2020.cn/wechat/patient/autoLogin

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：利用获得的session_key和用户授权得到的信息来后台换取用户手机号，自动登录并得到sessionId

    * 请求参数说明：

        | 名称 | 类型  |说明|
        |----- |------| ----|
        |encrypdata|String|用户授权获取的信息|
        |ivdata|String|用户授权获取的信息|
        |openid|String|上一个接口的信息|
        

    * 返回参数说明：

        | 名称 | 类型 |说明|
        |----- |------|----|
        |sessionId |String|用户登录以后保存登录信息的sessionId|
        |errorCode| int|错误码:200;503;502|
        |errorMsg| String|内容：登录成功;登录失败;查询电话失败|
        
---
       
 	
        
  * <span id = "five">身体信息增加或修改</span>

    * 接口地址：http://cdn.cmas2020.cn/wechat/bodyInfo/saveorupdateInfo

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：增加或修改信息

    * 请求参数说明：

        | 名称 | 类型  |说明|
        |----- |------| ----|
        |appetite|int|食欲|
        |datetime|Date|时间|
        |pain|int|疼痛|
        |sleep|int|睡眠|
        |weight|int|体重|


    * 返回参数说明：

        | 名称 | 类型 |说明|
        |errorCode| int|错误码:200|
        |errorMsg| String|内容：插入成功/更新成功|
        
---   

* <span id = "five">身体信息增加或修改</span>

    * 接口地址：http://cdn.cmas2020.cn/wechat/bodyInfo/retrieveInfo

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：查询信息

    * 请求参数说明：

        | 名称 | 类型  |说明|
        |----- |------| ----|
        |datetime|Date|时间|
        


    * 返回参数说明：

        | 名称 | 类型 |说明|
        |----- |------| ----|
        |errorCode|int|错误码:200，502|
        |errorMsg|String|内容：查询成功；未查到信息（代表还没加入过信息，下面四个参数均在查询成功才存在）|
        |appetite|int|食欲|
        |pain|int|疼痛|
        |sleep|int|睡眠|
        |weight|int|体重|
        
        
---   

 
