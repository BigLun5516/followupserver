



 ## 接口列表

|  接口  | 说明 |
|------ |----- |
|[https://cdn.cmas2020.cn/diary/uploadDiaryImg](#first)| 上传图片文件|
|[https://cdn.cmas2020.cn/diary/saveDiary](#second)| 保存更新日记|
|[https://cdn.cmas2020.cn/diary/getDiary](#third)|根据Id获取日记|
|[https://cdn.cmas2020.cn/diary/deleteDiary](#four)| 根据Id删除日记|
|[https://cdn.cmas2020.cn/diary/getAllDiaries](#five)| 获得所有日记|



***


## 接口详情
* <span id = "first">上传图片文件</span>

    * 接口地址：https://cdn.cmas2020.cn/diary/uploadDiaryImg

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：上传文件

      * 请求参数说明：

       | 名称 | 类型  |说明|
        |----- |------| ----|
        |file||图片文件|
    
    * 返回参数说明：

        | 名称 | 类型 |说明|
        |----- |------|----|
        | errorCode| int|错误码:200;502
        |errorMsg| String|内容：上传成功;上传失败
        |imgUrl| String|图片地址
        
---

* <span id = "second">保存更新日记</span>

    * 接口地址：https://cdn.cmas2020.cn/diary/saveDiary

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：保存更新日记

    * 请求参数说明：

       | 名称 | 类型  |说明|
        |----- |------| ----|
        |Id|Long|日记id，-1为新建日记，正数为更新日记|
        |mood| String| 心情|
        |time| String| 时间戳|
        |brief| String| 日记简介|
        |html| String| 日记内容|
        |img| String| 图片|

    * 返回参数说明：

         | 名称 | 类型 |说明|
        |----- |------|----|
        | errorCode| int|错误码:200;504
        |errorMsg| String|内容：保存成功;不存在这篇日记
        | id| Long|日记id
        



---
* <span id = "third">根据id获取日记</span>

    * 接口地址：https://cdn.cmas2020.cn/diary/getDiary

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：根据id获取日记

    * 返回参数说明：

         | 名称 | 类型 |说明|
        |----- |------|----|
        |Id |Long|日记id|
        |mood |String|心情|
        |time |String|时间戳|
        |html |String|日记内容|
        | errorCode| int|错误码:200;504
        |errorMsg| String|内容：查找成功;不存在这篇日记
        



---
* <span id = "four">删除日记</span>

    * 接口地址：https://cdn.cmas2020.cn/diary/deleteDiary

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：删除日记


    * 返回参数说明：

         | 名称 | 类型 |说明|
        |----- |------|----|
        | errorCode| int|错误码:200;504
        |errorMsg| String|内容：删除成功;不存在这篇日记
        



---
* <span id = "five">获得所有日记</span>

    * 接口地址：https://cdn.cmas2020.cn/diary/getAllDiaries

    * 返回格式：Json

    * 请求方式：Post

    * 接口备注：获得所有日记


    * 返回参数说明：

         | 名称 | 类型 |说明|
        |----- |------|----|
        | diaries| List|所有日记内容（包括id,img,mood,brief,time）
        | errorCode| int|错误码:200
        |errorMsg| String|内容：查找成功
        
