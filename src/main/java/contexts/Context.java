package contexts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 新版上下文
 * @author by.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Context {
    private String userNo;//用户编号
    private String admin;//管理员权限表征
    private String ipAddress;//ip地址
}
