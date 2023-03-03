package com.lgguan.iot.position.aop

import com.lgguan.iot.position.bean.IErrorCode
import com.lgguan.iot.position.bean.RoleType
import com.lgguan.iot.position.bean.failedOf
import com.lgguan.iot.position.util.getRole
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.stereotype.Component


/**
 * 权限校验
 *
 * @author N.Liu
 **/
@Retention
@Target(AnnotationTarget.FUNCTION)
annotation class HasPermission(
    val roles: Array<RoleType>
)

@Aspect
@Component
class PermissionAspect : Ordered {
    private val log = LoggerFactory.getLogger(javaClass)

    @Around(value = "@annotation(com.lgguan.iot.position.aop.HasPermission) && @annotation(hasPermission)")
    fun around(joinPoint: ProceedingJoinPoint, hasPermission: HasPermission): Any {
        val role = getRole()
        if (role == null || !hasPermission.roles.contains(role)) {
            log.error("Not has permission")
            return failedOf<Void>(IErrorCode.PermissionDenied)
        }
        return joinPoint.proceed()
    }

    override fun getOrder(): Int {
        return 100
    }
}
