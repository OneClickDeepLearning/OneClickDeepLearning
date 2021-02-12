package acceler.ocdl.service;

import acceler.ocdl.entity.Suffix;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SuffixService {
    Suffix createSuffix(Suffix suffix);

    Page<Suffix> getSuffix(Suffix suffix, int page, int size);

    boolean batchDeleteSuffix(List<Suffix> suffixs);
}
