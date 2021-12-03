DROP TABLE IF EXISTS public.diagnostic_data;
DROP TABLE IF EXISTS public.encoder_reading;
DROP TABLE IF EXISTS public.imu_reading;
DROP TABLE IF EXISTS public.lidar_reading;
DROP TABLE IF EXISTS public.location;
DROP TABLE IF EXISTS public.point_cloud;
DROP TABLE IF EXISTS public.vehicle;

CREATE TABLE public.vehicle (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    vehicle_name varchar(255),
    connection_date timestamp without time zone
);

CREATE TABLE public.diagnostic_data (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    vehicle_id bigint,
    reading_date timestamp without time zone,
    battery_charge_status real,
    camera_turn_angle real,
    wheels_turn_measure real,
    foreign key (vehicle_id) references public.vehicle(id)
);

CREATE TABLE public.encoder_reading (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    vehicle_id bigint,
    reading_date timestamp without time zone,
    left_front_wheel_speed double precision,
    left_rear_wheel_speed double precision,
    right_front_wheel_speed double precision,
    right_rear_wheel_speed double precision,
    foreign key (vehicle_id) references public.vehicle(id)
);

CREATE TABLE public.imu_reading (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    vehicle_id bigint,
    reading_date timestamp without time zone,
    acceleration_x double precision,
    acceleration_y double precision,
    acceleration_z double precision,
    angular_velocity_x double precision,
    angular_velocity_y double precision,
    angular_velocity_z double precision,
    magnetic_field_x double precision,
    magnetic_field_y double precision,
    magnetic_field_z double precision,
    foreign key (vehicle_id) references public.vehicle(id)
);

CREATE TABLE public.lidar_reading (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    vehicle_id bigint,
    reading_date timestamp without time zone,
    lidar_distances_reading varchar(16384),
    foreign key (vehicle_id) references public.vehicle(id)
 );

CREATE TABLE public.location (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    vehicle_id bigint,
    reading_date timestamp without time zone,
    real_x_coordinate double precision,
    real_y_coordinate double precision,
    slam_rotation double precision,
    slam_x_coordinate double precision,
    slam_y_coordinate double precision,
    foreign key (vehicle_id) references public.vehicle(id)
);

CREATE TABLE public.point_cloud (
    id bigint NOT NULL PRIMARY KEY AUTO_INCREMENT,
    vehicle_id bigint,
    reading_date timestamp without time zone,
    point_cloud_reading varchar(1048576), --PostgreSQL: text
    foreign key (vehicle_id) references public.vehicle(id)
);
