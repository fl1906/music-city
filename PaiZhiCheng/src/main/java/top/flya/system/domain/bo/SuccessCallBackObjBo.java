package top.flya.system.domain.bo;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: 风离
 * @Date: 2022/06/04/2:57
 * @Description:
 */
@Data
public class SuccessCallBackObjBo {

    private String id;

    private String create_time;

    private String event_type;

    private String resource_type;

    private Resource resource;

    private String summary;
}
