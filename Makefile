CC = g++
CFLAGS = -g -Wall
LIBS = cgicc
STD = c++17
TARGET = index

all: $(TARGET)

$(TARGET): $(TARGET).cpp
	$(CC) $(CFLAGS) -std=$(STD) -l $(LIBS) -o $(TARGET).cgi $(TARGET).cpp


clean:
	$(RM) $(TARGET).cgi