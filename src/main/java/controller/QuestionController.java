package controller;

import DataBase.DbUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Controller
public class QuestionController {
    @RequestMapping({"/GetQuestion.do"})
    public ModelAndView GetQuestion(@RequestBody String strjson)
    {
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        try
        {
            JSONObject jsonObject = JSONObject.fromObject(strjson);
            if(jsonObject.has("wx_openid")&&jsonObject.has("Guanka")){
                String [][]gettm =  DbUtils.getqabyguanka(jsonObject.getString("wx_openid"),jsonObject.getInt("Guanka"));
                JSONArray array = new JSONArray();
                for(int i=0;i<gettm.length-2;i++){
                    JSONObject questionobject = new JSONObject();
                    questionobject.put("id",i+1);
                    questionobject.put("question_describe",gettm[0][i]);
                    questionobject.put("answer_A",gettm[1][i]);
                    questionobject.put("answer_B",gettm[2][i]);
                    questionobject.put("answer_C",gettm[3][i]);
                    questionobject.put("answer_D",gettm[4][i]);
                    questionobject.put("answer_right",gettm[5][i]);
                    questionobject.put("answer_hint",gettm[6][i]);
                    if(questionobject==null){
                        break;
                    }
                    array.add(questionobject);
                }
                mav.addObject("result","ok");
                System.out.println("获取题目成功");
                mav.addObject("info",array);
            }

            else{
                mav.addObject("result","failed");

                mav.addObject("reason","key is not true");
            }







        }
        catch (Exception e)
        {

            mav.addObject("result", "failed");
            mav.addObject("reason","exception");
        }


        return mav;
    }



    @RequestMapping({"/GetQuestion2.do"})
    public ModelAndView GetQuestion2(@RequestBody String strjson)
    {
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        try
        {
            JSONObject jsonObject = JSONObject.fromObject(strjson);
            if(jsonObject.has("wx_openid")&&jsonObject.has("Guanka")&&jsonObject.has("tishu")){
                String [][]gettm =  DbUtils.getqabyguanka2(jsonObject.getString("wx_openid"),jsonObject.getInt("Guanka"),jsonObject.getInt("tishu"));
                JSONArray array = new JSONArray();
                for(int i=0;i<gettm[0].length;i++){
                    JSONObject questionobject = new JSONObject();
                    questionobject.put("id",i+1);
                    questionobject.put("question_describe",gettm[0][i]);
                    questionobject.put("answer_A",gettm[1][i]);
                    questionobject.put("answer_B",gettm[2][i]);
                    questionobject.put("answer_C",gettm[3][i]);
                    questionobject.put("answer_D",gettm[4][i]);
                    questionobject.put("answer_right",gettm[5][i]);
                    questionobject.put("answer_hint",gettm[6][i]);
                    if(questionobject==null){
                        break;
                    }
                    array.add(questionobject);
                }
                mav.addObject("result","ok");
                System.out.println("获取题目2成功");
                mav.addObject("info",array);
            }

            else{
                mav.addObject("result","failed");

                mav.addObject("reason","key is not true");
            }







        }
        catch (Exception e)
        {e.printStackTrace();
            mav.addObject("result", "failed");
            mav.addObject("reason","exception");
        }


        return mav;
    }




    @RequestMapping({"/UpdateGuanka.do"})
    public ModelAndView UpdateGuanka(@RequestBody String strjson)
    {
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        JSONObject jsonObject = JSONObject.fromObject(strjson);
        if(jsonObject.has("wx_openid") && jsonObject.has("update_guanka")){
            try
            {

                boolean issuccess= DbUtils.updateuserguanka(jsonObject.getString("wx_openid"),jsonObject.getInt("update_guanka"));
                if(issuccess){
                    mav.addObject("result","ok");
                }
                else{
                    mav.addObject("result","failed");
                    mav.addObject("reason","update guanka failed");
                }


                //  mav.addObject("info",array);



            }
            catch (Exception e)
            {

                mav.addObject("result", "failed");
                mav.addObject("reason","exception");
            }
        }




        return mav;
    }





}
