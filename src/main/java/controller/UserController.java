package controller;


import DataBase.DbUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * Created by Administrator on 2019/2/2.
 */

@Controller

public class UserController {




    @RequestMapping(method = {RequestMethod.GET}, value = {"/"})
    public String getserverinfo(Model model){
        System.out.println("into controller!");

        return "views/index.jsp";
    }





    @RequestMapping(method = {RequestMethod.POST}, value = {"/HasUser.do"})
    @ResponseBody
    public ModelAndView HasUser(@RequestBody String jsonstr){
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        JSONObject object = JSONObject.fromObject(jsonstr);
        if(object.has("wx_openid")){
            boolean exists =   DbUtils.getuserexist(object.getString("wx_openid"));

            if(exists == true){

                mav.addObject("result","ok");
            }
            else
                mav.addObject("result","none");

        }
        else
            mav.addObject("result","none");

        return  mav;
    }



    @RequestMapping(method = {RequestMethod.POST}, value = {"/GetRankingList.do"})
    @ResponseBody
    public ModelAndView GetRankingList(@RequestBody String jsonstr){
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        JSONObject object = JSONObject.fromObject(jsonstr);

        if(object.has("wx_openid")){
          //  System.out.println("stu_name:"+object.getString("stu_name"));
            String pm[][] =   DbUtils.getranklist(object.getString("wx_openid"));
            System.out.println(pm.length);
            if(pm!=null && pm[0][0]!=""){

                mav.addObject("result","ok");
                JSONArray array = new JSONArray();
                for(int i=0;i<Integer.valueOf(pm[4][0]);i++){
                    JSONObject questionobject = new JSONObject();
                    questionobject.put("user_stuname",pm[0][i]);
                    questionobject.put("user_stunum",pm[1][i]);
                    questionobject.put("user_guanka",pm[2][i]);
                    questionobject.put("user_photo",pm[3][i]);
                    

                    if(questionobject==null){
                        break;
                    }
                    array.add(questionobject);
                }
                mav.addObject("info",array);
                System.out.println("获取排行榜信息成功");
            }
            else{
                mav.addObject("result","failed");
                mav.addObject("reason","user have been exists in system");
            }


        }
        else{
            mav.addObject("result","failed");
            mav.addObject("reason","key is not true");
        }


        return  mav;
    }


    @RequestMapping(method = {RequestMethod.POST}, value = {"/GetGuankaTotal.do"})
    @ResponseBody
    public ModelAndView GetGuankaTotal(@RequestBody String jsonstr){
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        JSONObject object = JSONObject.fromObject(jsonstr);

        if(object.has("wx_openid")){
            //  System.out.println("stu_name:"+object.getString("stu_name"));
            int count =   DbUtils.getguankatotal(object.getString("wx_openid"));


                mav.addObject("result","ok");
                mav.addObject("count",count);



        }
        else{
            mav.addObject("result","failed");
            mav.addObject("reason","key is not true");
        }


        return  mav;
    }




    @RequestMapping(method = {RequestMethod.POST}, value = {"/AddUser.do"})
    @ResponseBody
    public ModelAndView AddUser(@RequestBody String jsonstr){
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        JSONObject object = JSONObject.fromObject(jsonstr);

        if(object.has("wx_openid") && object.has("stu_name") && object.has("stu_num")&&object.has("user_photo")){
            System.out.println("stu_name:"+object.getString("stu_name"));
            boolean success =   DbUtils.adduser(object.getString("wx_openid"),object.getString("stu_name"),object.getString("stu_num"),object.getString("user_photo"));

            if(success == true){

                mav.addObject("result","ok");
            }
            else{
                mav.addObject("result","failed");
                mav.addObject("reason","user have been exists in system");
            }


        }
        else{
            mav.addObject("result","failed");
            mav.addObject("reason","key is not true");
        }


        return  mav;
    }


    @RequestMapping(method = {RequestMethod.POST}, value = {"/wxlogin.do"})
    @ResponseBody
    public ModelAndView wxlogin(@RequestBody String jsonstr1){
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        JSONObject object =JSONObject.fromObject(jsonstr1);

        if(object.has("jscode")){
            String result =DbUtils.getsession(object.getString("jscode"));
            JSONObject o1 = JSONObject.fromObject(result);
            if(o1.has("errcode")){
                mav.addObject("result","failed");
            }
            else{
                mav.addObject("result","ok");
            }

            mav.addObject("info",result);System.out.println("tencent receive info:"+result);
        }
else{
            mav.addObject("result","failed");
            mav.addObject("reason","no jscode");
        }
        return  mav;
    }



    @RequestMapping(method = {RequestMethod.POST}, value = {"/GetUserInfo.do"})
    @ResponseBody
    public ModelAndView login(@RequestBody String jsonstr){
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        JSONObject object = JSONObject.fromObject(jsonstr);
        if(object.has("openid")){
            String [] res =  DbUtils.getuserinfo(object.getString("openid"));
            if(res[0]!=null &&res[1]!=null &&res[2]!=null){
                mav.addObject("result","ok");
                mav.addObject("user_stuname",res[0]);
                mav.addObject("user_stunum",res[1]);
                mav.addObject("user_guanka",res[2]);


            }

            else{
                mav.addObject("result","failed");
                mav.addObject("reason","this openid not found in system");
            }
        }
        else{
            mav.addObject("result","failed");
            mav.addObject("reason","the key 'openid' was not found");
        }


        return  mav;
    }




}
