package com.buyi.spzx.manager.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.buyi.spzx.manager.service.ValidateCodeService;
import com.buyi.spzx.model.vo.system.ValidateCodeVo;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.buyi.spzx.common.constant.RedisKey.VALIDATECODE_KEY;

@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 生成验证码
     *
     * @return
     */
    @Override
    public ValidateCodeVo generateValidateCode() {
        // 1.通过hutool工具生成验证码
        // 参数：宽  高  验证码位数 干扰线数量
        CircleCaptcha circleCaptcha =
                CaptchaUtil.createCircleCaptcha(150, 48, 4, 10);
        String codeValue = circleCaptcha.getCode(); // 生成的验证码的值
        String imageBase64 = circleCaptcha.getImageBase64(); // 验证码图片，进行了base64编码

        // 2.把验证码存放到redis中，key: uuid  value: 验证码的值
        // 设置过期时间
        String codeKey = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue()
                .set(VALIDATECODE_KEY + codeKey, codeValue, 3, TimeUnit.MINUTES);

        // 3.构建响应结果数据
        ValidateCodeVo validateCodeVo = new ValidateCodeVo() ;
        validateCodeVo.setCodeKey(codeKey); // redis中存储数据的key
        validateCodeVo.setCodeValue("data:image/png;base64," + imageBase64); // 验证码图片
        return validateCodeVo;
    }
}
