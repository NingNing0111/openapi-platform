package me.pgthinker.provider.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.pgthinker.common.BaseResponse;
import me.pgthinker.common.ResultUtils;
import me.pgthinker.provider.constant.RemoteAPI;
import me.pgthinker.provider.model.vo.animal.CatImageVo;
import me.pgthinker.provider.model.vo.animal.DogImageVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * @Project: me.pgthinker.provider.controller
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/7/28 16:07
 * @Description:
 */
@RestController
@RequestMapping("/animal")
@RequiredArgsConstructor
@Slf4j
public class AnimalController {

    private final RestTemplate restTemplate;
    @GetMapping("/cat/image")
    public BaseResponse randomCatImage(){
        UriComponentsBuilder ucb = UriComponentsBuilder.fromHttpUrl(RemoteAPI.RANDOM_CAT_IMAGE);
        ucb.queryParam("limit",1);
        List<CatImageVo> result = restTemplate.getForObject(ucb.toUriString(), List.class);
        log.info(" result:{}",  result);
        return ResultUtils.success(result.get(0));
    }

    @GetMapping("/dog/image")
    public BaseResponse randomDogImage(){
        DogImageVo forObject = restTemplate.getForObject(RemoteAPI.RANDOM_DOG_IMAGE, DogImageVo.class);
        return ResultUtils.success(forObject);
    }
}
