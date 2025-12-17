package org.example.tamaapi.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Embeddable
<<<<<<< HEAD
<<<<<<< HEAD
=======
@ToString
>>>>>>> b5c94cf684565bcfb12724553ffc7966857c3c69
=======
@ToString
>>>>>>> b5c94cf684565bcfb12724553ffc7966857c3c69
public class UploadFile {

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String storedFileName;

}
