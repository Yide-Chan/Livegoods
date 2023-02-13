package com.livegoods.search.feign;

import com.livegoods.pojo.Houses;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 基于Openfeign实现远程调用服务
 */
@FeignClient("livegoods-details")
public interface LivegoodsDetailsService {
    @GetMapping("/getAll")
    List<Houses> getAll();
}
