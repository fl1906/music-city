package top.flya.system.domain.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateMoneyBo {

    private Long userId;

    private BigDecimal money;
}
