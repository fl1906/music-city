package top.flya.system.common;


import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.flya.system.domain.vo.*;
import top.flya.system.service.ISysOssService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class BatchUtils {
    @Resource
    private ISysOssService iSysOssService;



    // 假设这里有一个方法可以批量查询新的 imageUrl
    public  Map<Long, String> getNewImageUrls(List<String> imageUrls) {
//        List<Long> ossIds = imageUrls.stream().map(Long::parseLong).collect(Collectors.toList());

        List<Long> ossIds = imageUrls.stream()
            .map(url -> {
                try {
                    return Long.parseLong(url);
                } catch (NumberFormatException e) {
                    // Handle the exception, e.g. logging or skipping the invalid value
                    // You can also return a default value in case of invalid format
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        return iSysOssService.listByIds(ossIds).stream().collect(Collectors.toMap(SysOssVo::getOssId, SysOssVo::getUrl));
    }

    public List<PzcArtistVo> transformToPzcArtistVo(List<PzcArtistVo> artistList) {
        log.info("transform artistList start: {}", artistList);
        // 获取所有旧的 imageUrl
        List<String> oldImageUrls = artistList.stream()
            .map(PzcArtistVo::getImageUrl)
            .collect(Collectors.toList());

        // 批量查询新的 imageUrl
        Map<Long, String> newImageUrls = getNewImageUrls(oldImageUrls);

        // 使用 Stream API 进行处理
        return artistList.stream()
            // 对列表中的每个元素进行处理
            .map(artist -> {
                // 从 Map 中获取新的 imageUrl
                String newImageUrl = artist.getImageUrl().contains("http")?artist.getImageUrl():newImageUrls.get(Long.parseLong(artist.getImageUrl()));
                // 创建一个新的 PzcArtistVo 对象，使用查询到的新 imageUrl
                return new PzcArtistVo(
                    artist.getArtistId(),
                    artist.getName(),
                    newImageUrl,
                    artist.getDescription()
                );
            })
            // 将处理后的元素收集到一个新的 List 中
            .collect(Collectors.toList());
    }

    public  List<PzcOrganizerVo> transformToPzcOrganizerVo(List<PzcOrganizerVo> organizerList) {
        log.info("transform organizerList start: {}", organizerList);
        // 获取所有旧的 imageUrl
        List<String> oldImageUrls = organizerList.stream()
            .map(PzcOrganizerVo::getLogo)
            .collect(Collectors.toList());

        // 批量查询新的 imageUrl
        Map<Long, String> newImageUrls = getNewImageUrls(oldImageUrls);

        // 使用 Stream API 进行处理
        return organizerList.stream()
            // 对列表中的每个元素进行处理
            .map(organizer -> {
                if(organizer.getLogo()==null){
                    return organizer;
                }
                // 从 Map 中获取新的 imageUrl
                String newImageUrl = organizer.getLogo().contains("http")?organizer.getLogo():newImageUrls.get(Long.parseLong(organizer.getLogo()));
                // 创建一个新的 PzcArtistVo 对象，使用查询到的新 imageUrl
                return new PzcOrganizerVo(
                    organizer.getOrganizerId(),
                    organizer.getPhone(),
                    organizer.getName(),
                    newImageUrl,
                    organizer.getContent(),
                    organizer.getCreateTime(),
                    organizer.getUpdateTime()
                );
            })
            // 将处理后的元素收集到一个新的 List 中
            .collect(Collectors.toList());
    }

    public  List<PzcTagVo> transformToPzcTagVo(List<PzcTagVo> tagList) {
        log.info("transform tagList start: {}", tagList);
        // 获取所有旧的 imageUrl
        List<String> oldImageUrls = tagList.stream()
            .map(PzcTagVo::getImageUrl)
            .collect(Collectors.toList());

        // 批量查询新的 imageUrl
        Map<Long, String> newImageUrls = getNewImageUrls(oldImageUrls);

        // 使用 Stream API 进行处理
        return tagList.stream()
            // 对列表中的每个元素进行处理
            .map(tag -> {
                // 从 Map 中获取新的 imageUrl
                String newImageUrl = tag.getImageUrl().contains("http")?tag.getImageUrl():newImageUrls.get(Long.parseLong(tag.getImageUrl()));
                // 创建一个新的 PzcArtistVo 对象，使用查询到的新 imageUrl
                return new PzcTagVo(
                    tag.getTagId(),
                    tag.getName(),
                    newImageUrl,
                    tag.getCreateTime(),
                    tag.getUpdateTime()
                );
            })
            // 将处理后的元素收集到一个新的 List 中
            .collect(Collectors.toList());
    }

    public List<PzcIntroVo> transformToPzcIntroVo(List<PzcIntroVo> introList){
        log.info("transform introList start: {}", JSONUtil.toJsonPrettyStr(introList));
        // 获取所有旧的 imageUrl
        List<String> oldImageUrls = introList.stream()
            .map(PzcIntroVo::getImageFullUrl)
            .collect(Collectors.toList());
        // 批量查询新的 imageUrl
        Map<Long, String> newImageUrls = getNewImageUrls(oldImageUrls);
        // 使用 Stream API 进行处理
        return introList.stream()
            // 对列表中的每个元素进行处理
            .map(intro -> {
                // 从 Map 中获取新的 imageUrl
                String newImageUrl = intro.getImageFullUrl().contains("http")?intro.getImageFullUrl():newImageUrls.get(Long.parseLong(intro.getImageFullUrl()));
                // 创建一个新的 PzcArtistVo 对象，使用查询到的新 imageUrl
                return new PzcIntroVo(
                    intro.getIntroId(),
                    intro.getTitle(),
                    intro.getContent(),
                    intro.getType(),
                    newImageUrl,
                    intro.getCreateTime(),
                    intro.getUpdateTime()

                );
            })
            // 将处理后的元素收集到一个新的 List 中
            .collect(Collectors.toList());

    }

    public List<PzcUserVo> transformToPzcUserVo(List<PzcUserVo> userList){
        log.info("transform userList start: {}", JSONUtil.toJsonPrettyStr(userList));
        // 获取所有旧的 imageUrl
        List<String> oldImageUrls = userList.stream()
            .filter(user -> user.getAvatar() != null&&!user.getAvatar().contains("http"))
            .map(PzcUserVo::getAvatar)
            .collect(Collectors.toList());
        // 批量查询新的 imageUrl
        Map<Long, String> newImageUrls = getNewImageUrls(oldImageUrls);
        // 使用 Stream API 进行处理
        return userList.stream()
            // 对列表中的每个元素进行处理
            .map(user -> {
                // 从 Map 中获取新的 imageUrl
                if(user.getAvatar()!=null&&!user.getAvatar().contains("http")) //如果是http开头的就不用转换了
                {
                    String newImageUrl = newImageUrls.get(Long.parseLong(user.getAvatar()));
                    // 创建一个新的 PzcArtistVo 对象，使用查询到的新 imageUrl
                    return new PzcUserVo(
                        user.getUserId(),
                        user.getOpenid(),
                        user.getMoney(),
                        user.getUserLevel(),
                        user.getIntegration(),
                        user.getIntegrationNow(),
                        user.getRealname(),
                        user.getNickname(),
                        user.getSex(),
                        user.getPhone(),
                        newImageUrl,
                        user.getAddress(),
                        user.getIntro(),
                        user.getAge(),
                        user.getConstellation(),
                        user.getMbti(),
                        user.getHobby(),
                        user.getSchool(),
                        user.getOccupation(),
                        user.getCreateTime(),
                        user.getUpdateTime(),
                        user.getMusicStyle(),
                        user.getState(),
                        user.getExemptCancel()
                    );
                }
                return user;

            })
            // 将处理后的元素收集到一个新的 List 中
            .collect(Collectors.toList());

    }

    public List<PzcViewPagerVo> transformToPzcViewPagerVo(List<PzcViewPagerVo> viewPagerList)
    {
//        log.info("transform viewPagerList start: {}", JSONUtil.toJsonPrettyStr(viewPagerList));
        // 获取所有旧的 imageUrl
        List<String> oldImageUrls = viewPagerList.stream()
            .map(PzcViewPagerVo::getImageUrl)
            .collect(Collectors.toList());
        // 批量查询新的 imageUrl
        Map<Long, String> newImageUrls = getNewImageUrls(oldImageUrls);
        // 使用 Stream API 进行处理
        return viewPagerList.stream()
            // 对列表中的每个元素进行处理
            .map(viewPager -> {
                // 从 Map 中获取新的 imageUrl
                String newImageUrl = viewPager.getImageUrl().contains("http")?viewPager.getImageUrl():newImageUrls.get(Long.parseLong(viewPager.getImageUrl()));
                // 创建一个新的 PzcArtistVo 对象，使用查询到的新 imageUrl
                return new PzcViewPagerVo(
                    viewPager.getViewPagerId(),
                    viewPager.getName(),
                    newImageUrl,
                    viewPager.getLinkUrl(),
                    viewPager.getState(),
                    viewPager.getActivityId()
                );
            })
            // 将处理后的元素收集到一个新的 List 中
            .collect(Collectors.toList());

    }


    public List<PzcActivityVo> transformToPzcActivityVo(List<PzcActivityVo> records) {
        log.info("transform activityList start: {}", JSONUtil.toJsonPrettyStr(records));
        // 获取所有旧的 imageUrl
        List<String> oldImageUrls = records.stream()
            .map(PzcActivityVo::getCoverImage)
            .collect(Collectors.toList());

        List<String> innerImageUrls = records.stream()
            .map(PzcActivityVo::getInnerImage)
            .collect(Collectors.toList());

        List<String> shareImageUrls = records.stream()
            .filter(activity -> activity.getShareImage() != null)
            .map(PzcActivityVo::getShareImage)
            .collect(Collectors.toList());
        // 批量查询新的 imageUrl
        Map<Long, String> newImageUrls = getNewImageUrls(oldImageUrls);
        Map<Long, String> newInnerImageUrls = getNewImageUrls(innerImageUrls);
        Map<Long, String> newShareImageUrls = getNewImageUrls(shareImageUrls);
        // 使用 Stream API 进行处理
        return records.stream()
            .map(
                r->{
                    String newImageUrl = r.getCoverImage().contains("http")?r.getCoverImage():newImageUrls.get(Long.parseLong(r.getCoverImage()));
                    String innerImage = r.getInnerImage().contains("http")?r.getInnerImage():newInnerImageUrls.get(Long.parseLong(r.getInnerImage()));
                    String shareImage = r.getShareImage()==null?null:r.getShareImage().contains("http")?r.getShareImage():newShareImageUrls.get(Long.parseLong(r.getShareImage()));
                    return  new PzcActivityVo(
                        r.getActivityId(),
                        r.getAddress(),
                        r.getRegionId(),
                        r.getTitle(),
                        r.getStartTime(),
                        r.getEndDate(),
                        innerImage,
                        r.getShowTime(),
                        newImageUrl,
                        r.getCreateTime(),
                        r.getUpdateTime(),
                        r.getState(),
                        r.getOrganizerId(),
                        r.getClassify(),
                        r.getRegion(),
                        null,
                        null,
                        null,
                        null,
                        null,
                        shareImage
                    );
                }
            ).collect(Collectors.toList());
    }
}
