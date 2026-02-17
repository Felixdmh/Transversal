package Controlador;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HiloVotanteTest {

  @Test
  void calcularPartido_18_25_numero30_devuelvePP() {
    // Arrange
    HiloVotante hv = new HiloVotante("Madrid", "18_25", null, null);

    // Act
    String partido = hv.calcularPartido(30);

    // Assert
    assertEquals("PP", partido);
  }

  @Test
  void calcularPartido_18_25_numero51_devuelveVOX() {
    // Arrange
    HiloVotante hv = new HiloVotante("Madrid", "18_25", null, null);

    // Act
    String partido = hv.calcularPartido(51);

    // Assert
    assertEquals("VOX", partido);
  }

  @Test
  void calcularPartido_MAS_66_numero96_devuelveSALF() {
    // Arrange
    HiloVotante hv = new HiloVotante("Madrid", "MAS_66", null, null);

    // Act
    String partido = hv.calcularPartido(96);

    // Assert
    assertEquals("SALF", partido);
  }
}
