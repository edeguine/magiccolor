CXX=g++

CXXFLAGS=-g -std=c++11 `libpng-config --cflags` -I/Users/etienne/Magiccolor/cpp/src
LDFLAGS=-g `libpng-config --ldflags` -L/Users/etienne/Magiccolor/cpp/src

SRC_DIR=../src
OBJ_DIR=./obj
SRC_FILES=$(wildcard $(SRC_DIR)/*.cpp)
OBJ_FILES=$(patsubst $(SRC_DIR)/%.cpp,$(OBJ_DIR)/%.o,$(SRC_FILES))

OBJECTS=$(patsubst %.cpp,obj/%.o,$(SRC)) 
$(OBJECTS): | obj
EXECUTABLE=core

info:
	$(info    VAR is $(SRC_FILES))

clean:
	rm -f obj/*.o
obj:
	mkdir -p $@

core: $(OBJ_FILES)
	g++ $(LDFLAGS) -o $@ $^

$(OBJ_DIR)/%.o: $(SRC_DIR)/%.cpp
	g++ $(CXXFLAGS) -c -o $@ $<
