package acceler.ocdl.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "platform_meta")
public class PlatformMeta {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "k8_url")
    @JsonProperty("k8_url")
    private String k8Url;

    @Column(name = "model_url")
    @JsonProperty("model_url")
    private String modelUrl;

    @Column(name = "template_url")
    @JsonProperty("template_url")
    private String templateUrl;

    @Column(name = "hadoop_url")
    @JsonProperty("hadoop_url")
    private String hadoopUrl;

    @Column(name = "kafka_url")
    @JsonProperty("kafka_url")
    private String kafkaUrl;


}
