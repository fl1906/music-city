package top.flya.system.xxlJob.diy;


import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.flya.common.core.domain.R;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${xxl.job.admin.url.add}")
    private String addUrl;

    @Value("${xxl.job.admin.url.pageList}")
    public String pageListUrl;

    @Value("${xxl.job.admin.url.update}")
    public String updateUrl;

    @Value("${xxl.job.admin.url.start}")
    public String startUrl;

    @Value("${xxl.job.admin.url.stop}")
    public String stopUrl;
    @Autowired
    private JobLoginService jobLoginService;

    @GetMapping("/list")
    public List<XxlJobInfo> getJobList() {
        HttpResponse response = HttpRequest.post(pageListUrl)
            .form("jobGroup", 3)
            .form("triggerStatus", -1)
            .form("jobDesc", "")
            .form("executorHandler", "wxHandler")
            .form("start", 0)
            .form("length", 10)
            .form("author", "")
            .cookie(jobLoginService.getCookie())
            .execute();

        String body = response.body();
        JSONArray array = JSONUtil.parse(body).getByPath("data", JSONArray.class);
        List<XxlJobInfo> list = array.stream()
            .map(o -> JSONUtil.toBean((JSONObject) o, XxlJobInfo.class))
            .collect(Collectors.toList());
        return list;
    }

    @PostMapping("/add")
    public R createNewJob()
    {
        HttpResponse response = HttpRequest.post(addUrl)
            .form("jobGroup", 3)
            .form("jobDesc", "测试任务2333")
            .form("author", "flya")
            .form("alarmEmail", "")
            .form("scheduleType", "CRON")
            .form("scheduleConf", "0 0 0 * * ?")
            .form("cronGen_display", "0 0 0 * * ?")
            .form("schedule_conf_CRON","")
            .form("schedule_conf_FIX_RATE","")
            .form("schedule_conf_FIX_DELAY","")
            .form("glueType", "BEAN")
            .form("executorHandler", "wxHandler")
            .form("executorParam", "")
            .form("executorRouteStrategy", "FIRST")
            .form("childJobId", "")
            .form("misfireStrategy", "DO_NOTHING")

            .form("executorBlockStrategy", "SERIAL_EXECUTION")
            .form("executorTimeout", 0)
            .form("executorFailRetryCount", 0)
            .form("glueRemark", "GLUE代码初始化")
            .form("glueSource", "")

            .cookie(jobLoginService.getCookie())
            .execute();
        String body = response.body();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        if (jsonObject.getInt("code") == 200) {
            return R.ok();
        }else {
            return R.fail();
        }

    }


    @PostMapping("/update")
    public R updateNewJob()
    {
        HttpResponse response = HttpRequest.post(updateUrl)
            .form("jobGroup", 3)
            .form("jobDesc", "测试任务")
            .form("author", "flya2333")
            .form("alarmEmail", "")
            .form("scheduleType", "CRON")
            .form("scheduleConf", "0 0 0 * * ?")
            .form("cronGen_display", "0 0 0 * * ?")
            .form("schedule_conf_CRON","")
            .form("schedule_conf_FIX_RATE","")
            .form("schedule_conf_FIX_DELAY","")
            .form("glueType", "BEAN")
            .form("executorHandler", "wxHandler")
            .form("executorParam", "")
            .form("executorRouteStrategy", "FIRST")
            .form("childJobId", "")
            .form("misfireStrategy", "DO_NOTHING")

            .form("executorBlockStrategy", "SERIAL_EXECUTION")
            .form("executorTimeout", 0)
            .form("executorFailRetryCount", 0)
            .form("glueRemark", "GLUE代码初始化")
            .form("glueSource", "")
            .form("id", 5)

            .cookie(jobLoginService.getCookie())
            .execute();

        String body = response.body();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        if (jsonObject.getInt("code") == 200) {
            return R.ok();
        }else {
            return R.fail();
        }
    }

    @PostMapping("/start")
    public R startNewJob()
    {

        HttpResponse response = HttpRequest.post(startUrl+"?id=5")
            .cookie(jobLoginService.getCookie())
            .execute();
        String body = response.body();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        if (jsonObject.getInt("code") == 200) {
            return R.ok();
        }else {
            return R.fail();
        }
    }

    @PostMapping("/stop")
    public R stopNewJob()
    {
        HttpResponse response = HttpRequest.post(stopUrl+"?id=5")
            .cookie(jobLoginService.getCookie())
            .execute();
        String body = response.body();
        JSONObject jsonObject = JSONUtil.parseObj(body);
        if (jsonObject.getInt("code") == 200) {
            return R.ok();
        }else {
            return R.fail();
        }
    }

}
