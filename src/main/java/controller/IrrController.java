package controller;

import DataBase.DbUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import request.HttpThread;


import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

@Controller
public class IrrController {
    public static String urlhead="http://192.168.0.253/";
    private void readwait()
    {
        System.out.println("*********进入计时方法**************");
        int delay_once = 0;
        do
        {
            try
            {
                Thread.sleep(50L);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            delay_once++;
            if (HttpThread.succflag == 1) {
                return;
            }
        } while (

                delay_once < 80);
    }












    @RequestMapping(method = {RequestMethod.POST}, value = {"/getknowledge.do"})
    @ResponseBody
    public ModelAndView getknowledge(@RequestBody String jsonstr){
        Map map = DbUtils.getknowledge();
        ArrayList idlist = (ArrayList) map.get("id");
        ArrayList contentlist= (ArrayList) map.get("content");
        JSONArray array = new JSONArray();
        for(int i =0;i<idlist.size();i++){
            JSONObject object = new JSONObject();
            object.put("msgid",idlist.get(i));
            object.put("msgcontent",contentlist.get(i));
            array.add(object);
        }
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        mav.addObject("msgresp",array);
        return mav;

    }




    @RequestMapping({"/GetRealTimeSensorValues.do"})
    public ModelAndView GetRealTimeSensorValue(@RequestBody String strjson)
    {
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
      //  GetRealData gd = new GetRealData();
      //  strjson = gd.getrealdata();
        System.out.println(strjson);
        //readwait();

            try
            {
                JSONObject jsonObject = JSONObject.fromObject(strjson);
                System.out.println("##返回数值-webContent##" + jsonObject.toString());
                mav.addObject("result","ok");
                mav.addObject("Irrtemperature", Float.parseFloat(jsonObject.optString("temp")));
                mav.addObject("Irrhumidity", Float.parseFloat(jsonObject.optString("humi")));
                mav.addObject("Irrlight", Float.parseFloat(jsonObject.optString("light")));
                mav.addObject("Irrsoilhum", Float.parseFloat(jsonObject.optString("soil")));

                System.out.println("##返回数值-处理后webContent##" + mav.toString());
            }
            catch (Exception e)
            {
                System.err.println("###HttpThread.e ###：" + e.toString());
                mav.addObject("result", "failed");
                mav.addObject("reason","json format is not true");
            }


        return mav;
    }


}
