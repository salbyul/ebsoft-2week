package com.study.service.board;

import com.study.repository.BoardRepository;
import com.study.service.Service;

public interface BoardService extends Service {

    BoardRepository boardRepository = new BoardRepository();
}
