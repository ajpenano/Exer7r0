module service2 {
	requires org.apache.commons.io;
	requires transitive model2;
	requires transitive util2;
	exports com.exist.service2;
}