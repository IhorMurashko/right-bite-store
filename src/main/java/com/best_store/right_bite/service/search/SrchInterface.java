package com.best_store.right_bite.service.search;

import com.best_store.right_bite.dto.search.*;

import java.util.ArrayList;

public interface SrchInterface {
    ArrayList<Integer> search(SrchRequest srchRequest);
}
