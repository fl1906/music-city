package top.flya.system.mapper;

import org.springframework.stereotype.Component;
import top.flya.system.domain.vo.PzcRegionVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RegionTreeMapper {
    public  Map<String, List<PzcRegionVo>> buildTree(List<PzcRegionVo> pzcRegionVos) {
        List<PzcRegionVo> tree = new ArrayList<>();
        Map<String, PzcRegionVo> nodeMap = new HashMap<>();

        for (PzcRegionVo node : pzcRegionVos) {
            nodeMap.put(node.getBase(), node);
        }

        for (PzcRegionVo node : pzcRegionVos) {
            String parentBase = getParentBase(node);
            if (parentBase != null) {
                PzcRegionVo parentNode = nodeMap.get(parentBase);
                if (parentNode != null) {
                    parentNode.getChildren().add(node);
                }
            } else {
                tree.add(node);
            }
        }

        Map<String, List<PzcRegionVo>> resultMap = new HashMap<>();

        for (PzcRegionVo node : tree) {
            String base = node.getBase();
            if (resultMap.containsKey(base)) {
                resultMap.get(base).add(node);
            } else {
                List<PzcRegionVo> nodeList = new ArrayList<>();
                nodeList.add(node);
                resultMap.put(base, nodeList);
            }
        }
        resultMap.put("全部",pzcRegionVos);

        return resultMap;
    }

    private  String getParentBase(PzcRegionVo node) {
        String base = node.getBase();
        if (base != null && base.contains("-")) {
            return base.substring(0, base.lastIndexOf("-"));
        }
        return null;
    }

//    public static void main(String[] args) {
//        List<PzcRegionVo> pzcRegionVos = new ArrayList<>();
//// 添加示例数据
//        pzcRegionVos.add(new PzcRegionVo(1L, "江苏省", "南京市", "", null, null, null));
//        pzcRegionVos.add(new PzcRegionVo(2L, "江苏省", "苏州市", "", null, null, null));
//        pzcRegionVos.add(new PzcRegionVo(3L, "上海市", "上海市", "", null, null, null));
//
//
//        System.out.println(JSONUtil.toJsonPrettyStr( buildTree(pzcRegionVos)));
//    }
}
