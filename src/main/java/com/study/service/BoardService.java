package com.study.service;

import com.study.repository.BoardRepository;

public interface BoardService extends Service{

    BoardRepository boardRepository = new BoardRepository();
}
