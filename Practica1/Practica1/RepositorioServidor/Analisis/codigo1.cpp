#include <bits/stdc++.h>

using namespace std;



int main(){

    long long numOp = 0;

    vector<int> pruebas = {-987,-1,0,1,2,3,5,15,20,100,409,500,593,1000,1471,1500,2801,3000,5000,10000,20000};

    
    for(int n: pruebas){

        numOp = 0;
        
        for(int i = 10; i < n*5; i *= 2)
            numOp++;

        cout << numOp << endl;
    }



    return 0;
}

