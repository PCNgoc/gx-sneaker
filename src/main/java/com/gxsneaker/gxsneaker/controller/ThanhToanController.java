package com.gxsneaker.gxsneaker.controller;


import com.gxsneaker.gxsneaker.dto.ThanhToanDTO;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/thanh-toan")
@CrossOrigin("*")
public class ThanhToanController {



    @PostMapping
    public Object thanhToan(
            @RequestBody ThanhToanDTO dto
    ){


        if(dto.getPhuongThuc().equals("COD")){


            return "Đặt hàng thành công - thanh toán khi nhận hàng";


        }



        if(dto.getPhuongThuc().equals("VNPAY")){


            return "Chuyển sang VNPAY QR";


        }



        return "Phương thức không hợp lệ";


    }


}