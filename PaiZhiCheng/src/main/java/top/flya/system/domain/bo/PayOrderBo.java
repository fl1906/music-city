package top.flya.system.domain.bo;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PayOrderBo {

    @NotNull
    private Integer count;
}
