
 ## 医生端接口列表

|  接口  | 说明 |
|------ |----- |
|[https://cdn.cmas2020.cn/app/doctor/getIdentifyCode](#first)| 获取验证码|
|[https://cdn.cmas2020.cn/app/doctor/login/loginWithEmployeeNum](#second)| 通过职工号和密码登录|
|https://cdn.cmas2020.cn/app/doctor/login/loginWithIdentifyCode| 通过验证码登录 |
|[https://cdn.cmas2020.cn/app/doctor/bindTel](#third)|绑定手机号|
|[https://cdn.cmas2020.cn/app/doctor/editInfo](#four)| 医生信息修改|
|[https://cdn.cmas2020.cn/app/doctor/login/modifyPasswd](#five)| 修改密码|
|[https://cdn.cmas2020.cn/app/doctor/showDoctorInfo](#six)| 获取医生详细信息|

***


## 接口详情
* <span id = "first">获取验证码</span>

    * 接口地址：https://cdn.cmas2020.cn/app/doctor/getIdentifyCode

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：医生在绑定手机号，修改密码的时候都需要获取验证码

    * 请求参数说明：

        | 名称 | 类型  |说明|
        |----- |------| ----|
        |tel|String|手机号|
        |type | Int| 获取验证码的用途（0登录，1注册，2修改密码，3绑定手机号）|
    

    * 返回参数说明：

        | 名称      | 类型   | 说明                                                   |
        | --------- | ------ | ------------------------------------------------------ |
        | errorCode | int    | 状态码：200， 502                                      |
        | errorMsg  | String | 内容：发送成功，发送失败或者一分钟之内已经发送过验证码 |
---

* <span id = "second">通过职工号和密码登录</span>

    * 接口地址：https://cdn.cmas2020.cn/app/doctor/login/loginWithEmployeeNum

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：医生通过职工号和密码进行登录

    * 请求参数说明：

       | 名称 | 类型  |说明|
       |----- |------| ----|
       |employeeNum|String|职工号|
       |password| String| 经md5加密过的密码 |

    * 返回参数说明：

         | 名称      | 类型   | 说明                                   |
        | --------- | ------ | -------------------------------------- |
        | sessionId | String | 职工号和id对应的session                |
        | errorCode | int    | 状态码：200， 401                      |
        | errorMsg  | String | 内容：登录成功，职工号不存在或登录失败 |
        
---

* <span id = "second">通过验证码登录</span>
        
        * 接口地址：https://cdn.cmas2020.cn/app/doctor/login/loginWithIdentifyCode
        
        * 返回格式：Json
        
        * 请求方式：Post
        
        * 接口备注：直接通过验证码登录
        
        * 请求参数说明：
        
          | 名称 | 类型   | 说明   |
          | ---- | ------ | ------ |
          | tel  | String | 电话   |
          | code | String | 验证码 |
        
        * 返回参数说明：
        
          | 名称      | 类型   | 说明                                         |
          | --------- | ------ | -------------------------------------------- |
          | errorCode | int    | 状态码：200;504;505                          |
          | errorMsg  | String | 内容：登录成功;用户不存在;验证码错误或已失效 |

---
* <span id = "third">绑定手机号</span>

    * 接口地址：https://cdn.cmas2020.cn/app/doctor/bindTel

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：医生登录后绑定手机号

    * 请求参数说明：

       | 名称 | 类型  |说明|
       |----- |------| ----|
       |employeeNum|String|职工号|
       |code| String| 经md5加密过的密码|
   |tel| String | 手机号 |
   
* 返回参数说明：
  
         | 名称      | 类型   | 说明                                  |
        | --------- | ------ | ------------------------------------- |
        | errorCode | int    | 状态码：200， 501，503，504，505，506 |
        | errorMsg  | String | 内容：绑定成功，绑定失败              |

---
* <span id = "four">医生信息修改</span>

    * 接口地址：https://cdn.cmas2020.cn/app/doctor/editInfo

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：医生修改自己的头像或者专长信息

    * 请求参数说明：

       | 名称 | 类型  |说明|
       |----- |------| ----|
       |employeeNum|String|职工号|
       |photo| byte[] |图片的base64编码 |
       |speciality| String|专长 |

    * 返回参数说明：

         | 名称      | 类型   | 说明                             |
        | --------- | ------ | -------------------------------- |
        | errorCode | int    | 状态码：200，505，506            |
        | errorMsg  | String | 内容：修改信息成功，修改信息失败 |

---
* <span id = "five">医生修改密码</span>

    * 接口地址：https://cdn.cmas2020.cn/app/doctor/login/modifyPasswd

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：修改密码，需要输入验证码进行修改

    * 请求参数说明：

       | 名称 | 类型  |说明|
       |----- |------| ----|
       | employeeNum | String |职工号|
       |tel|String|电话|
       |password| String|经md5加密过的密码 |
   |code| String|验证码 |
   
* 返回参数说明：
  
         | 名称      | 类型   | 说明                             |
        | --------- | ------ | -------------------------------- |
        | errorCode | int    | 状态码：200，503，505，506       |
        | errorMsg  | String | 内容：修改密码成功，修改密码失败 |
---
* <span id = "six">获取医生详细信息信息</span>

    * 接口地址：https://cdn.cmas2020.cn/app/doctor/showDoctorInfo

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：用户填写个人信息

    * 请求参数说明：

    
    | 名称        | 类型   | 说明   |
    | ----------- | ------ | ------ |
    | employeeNum | String | 职工号 |
    
    * 返回参数说明：
    
         | 名称      | 类型       | 说明                             |
        | --------- | ---------- | -------------------------------- |
        | data      | DoctorInfo | DoctorInfo类对应的json字符串     |
        | errorCode | int        | 状态码：200，505                 |
        | errorMsg  | String     | 内容：查询信息成功，查询信息失败 |
        
        返回参数举例
        
        ```json
        {
            "errorCode": 200,
        "errorMsg": "查询用户信息成功",
	        "data": {
            "doctorId": 1,
                "employeeNum": "12345",
            "tel": "11111111111",
                "photo": null,
                "name": null,
                "department": null,
                "speciality": "抑郁症"
            }
        }
        ```
        
        

