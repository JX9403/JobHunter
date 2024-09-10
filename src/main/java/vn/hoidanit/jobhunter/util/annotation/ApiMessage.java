package vn.hoidanit.jobhunter.util.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Thoi diem hoat dong - khi chay project
@Retention(RetentionPolicy.RUNTIME)
// Pham vi hoat dong : method
@Target(ElementType.METHOD)
public @interface ApiMessage {
  String value();
}
