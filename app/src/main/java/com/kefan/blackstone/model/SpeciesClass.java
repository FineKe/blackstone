package com.kefan.blackstone.model;

import com.kefan.blackstone.database.Species;

import java.util.List;

public  class SpeciesClass {
        private int count;//多少种
        private String name;//名字，这里我没有赋值
        private String speciesType;
        private List<Species> list;//这个是主要数据，下一级界面用的就是这个list

        public SpeciesClass(String speciesType, List<Species> list, int count) {
            this.speciesType = speciesType;
            this.list = list;
            this.count = count;

        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSpeciesType() {
            return speciesType;
        }

        public void setSpeciesType(String speciesType) {
            this.speciesType = speciesType;
        }

        public List<Species> getList() {
            return list;
        }

        public void setList(List<Species> list) {
            this.list = list;
        }
    }
