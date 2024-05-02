package top.flya.system.domain.vo;

import lombok.Data;

@Data
public class RefurbishVO {


    //发起方当前位置
    private String startAddress;
    //申请方当前位置
    private String applyAddress;

    private Long applyId;

    private Long distance;
}
