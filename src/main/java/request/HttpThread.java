package request;

import request.HttpGetRequest;

public class HttpThread
  extends Thread
{
  public HttpGetRequest getPost() {
    return post;
  }

  public void setPost(HttpGetRequest post) {
    this.post = post;
  }

  public int getRes() {
    return res;
  }

  public void setRes(int res) {
    this.res = res;
  }

  int res;
  HttpGetRequest post;
  public static String getWebContent() {
    return webContent;
  }

  public static void setWebContent(String webContent) {
    HttpThread.webContent = webContent;
  }

  public static String webContent;
  public static int succflag;
  private String url;
 // private String jsonstring;


  public static int getSuccflag() {
    return succflag;
  }

  public static void setSuccflag(int succflag) {
    HttpThread.succflag = succflag;
  }

  public void run()
  {
   // System.out.println("thread start.....");
    System.out.println("****线程接收到的  url:" + this.url);
    
    webContent = "";
    succflag = 0;
    
    post = new HttpGetRequest();
    res = post.requestHttp(this.url);
    //System.err.println("****线程接收到的jsonstring:" + this.jsonstring);
    
  //  System.err.println("****线程接收到的res：" + res);
    if (res == 1)
    {
      webContent = post.getWebContext();
   //   System.err.println("****线程接收到的内容：" + webContent);
      succflag = 1;
    }
    else
    {
      webContent = "";
      succflag = 0;
    }
  }
  
  public String getUrl()
  {
    return this.url;
  }
  
  public void setUrl(String url)
  {
    this.url = url;
  }
  

}
