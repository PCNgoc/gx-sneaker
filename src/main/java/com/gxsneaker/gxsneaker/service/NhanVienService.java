package com.gxsneaker.gxsneaker.service;

import com.gxsneaker.gxsneaker.dto.NhanVienDTO;

import java.util.List;

public interface NhanVienService {

    List<NhanVienDTO> getAll();

    NhanVienDTO getById(Integer id);

    NhanVienDTO create(NhanVienDTO dto);

    NhanVienDTO update(Integer id, NhanVienDTO dto);

    void delete(Integer id);

}