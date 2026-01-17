package com.bank.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponse {
    private List<CustomerDTO> content;
    private Integer pageNumber;
    private Integer pageSize;
    private Long totalElements;

}
