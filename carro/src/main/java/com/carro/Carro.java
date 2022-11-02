package com.carro;

import com.middleware.*;
import com.middleware.annotations.*;
import com.middleware.annotations.Pool;
import com.middleware.communication.*;
import com.middleware.lifecycle_management.*;
import org.json.*;

@RequestMap(router = "/car")
public class Carro {
  private String nome;

  private String cor;

  @Lifecycle(strategy = Strategy.STATIC)
  @Pool(maximum = 100)
  @Get(router = "/getcolor")
  public String getCor() {
    return cor;
  }
  
  @Lifecycle(strategy = Strategy.OPTIMIZED_STATIC)
  @Post(router = "/setcolor")
  public void setCor(String cor) {
    this.cor = cor;
  }
  
  @Lifecycle(strategy = Strategy.PER_REQUEST)
  @Delete(router = "/getname")
  public String getNome() {
    return nome;
  }

  @Lifecycle(strategy = Strategy.OPTIMIZED_PER_REQUEST)
  @Put(router = "/setname")  
  public void setNome(String nome) {
    this.nome = nome;
  }
}
