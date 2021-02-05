/**
  * Copyright 2020 bejson.com 
  */
package com.epic.followup.temporary.sumian.wechatnoifyrequest;

/**
 * Auto-generated: 2020-02-06 17:16:39
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class JsonRootBean {

    private String touser;
    private String template_id;
    private String url;
    private Miniprogram miniprogram;
    private Data data;
    public void setTouser(String touser) {
         this.touser = touser;
     }
     public String getTouser() {
         return touser;
     }

    public void setTemplate_id(String template_id) {
         this.template_id = template_id;
     }
     public String getTemplate_id() {
         return template_id;
     }

    public void setUrl(String url) {
         this.url = url;
     }
     public String getUrl() {
         return url;
     }

    public void setMiniprogram(Miniprogram miniprogram) {
         this.miniprogram = miniprogram;
     }
     public Miniprogram getMiniprogram() {
         return miniprogram;
     }

    public void setData(Data data) {
         this.data = data;
     }
     public Data getData() {
         return data;
     }

}