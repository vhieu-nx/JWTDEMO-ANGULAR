package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Hieu} entity.
 */
public class HieuDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HieuDTO)) {
            return false;
        }

        HieuDTO hieuDTO = (HieuDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hieuDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HieuDTO{" +
            "id=" + getId() +
            "}";
    }
}
